package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.exceptions.*;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.services.PasswordService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/reset-password")
    public RedirectView resetPasswordPost(@RequestParam("username") String username, HttpServletRequest request, RedirectAttributes ra) {
        RedirectView rv = new RedirectView("/log-in");
        ra.addFlashAttribute("upperNotification", "Check your email address!");
        try {
            Account acc = passwordService.searchForUser(username);
            String hostLink = request.getScheme() + "://" + request.getServerName()+":8080";
            passwordService.addResetRequest(acc, hostLink);
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("upperNotification", "User not found!");
        } catch (SystemException e) {
            ra.addFlashAttribute("upperNotification", "There was a problem in sending the reset email!");
        }
        return rv;
    }

    @GetMapping("/validate-password-reset-request")
    public RedirectView validatePasswordResetRequestGet(@RequestParam("token") String token, RedirectAttributes ra) {
        RedirectView rv;
        try {
            rv = new RedirectView("/set-new-password");
            int id = passwordService.identifyAccount(token);
            ra.addFlashAttribute("userId", id);
        } catch (InvalidTokenException e) {
            rv = new RedirectView("/log-in");
            ra.addFlashAttribute("upperNotification", "The token expired or is invalid!");
        }
        return rv;
    }

    @GetMapping("/set-new-password")
    public ModelAndView setNewPasswordGet(@ModelAttribute("userId") int id) {
        ModelAndView mv = new ModelAndView("reset-password");
        mv.addObject("userId", id);
        return mv;
    }

    @PostMapping("/set-new-password")
    public ModelAndView setNewPasswordPost(@RequestParam Map<String, String> data, @ModelAttribute("userId") int id, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("reset-password");
        mv.addObject("userId", id);
        try {
            passwordService.validatePassword(data.get("password"), data.get("passwordVerification"));
            passwordService.updateAccountPasswordById(Integer.parseInt(data.get("userId")), data.get("password"));
            mv = new ModelAndView("redirect:/log-in");
            ra.addFlashAttribute("upperNotification", "Password updated!");
            return mv;
        } catch (WeakPasswordException e) {
            mv.addObject("error", "The password is too weak!");
        } catch (PasswordVerificationException e) {
            mv.addObject("error", "The password does not match!");
        } catch (SignUpException e) {
            mv.addObject("error", "We're having some system issues! Try again later!");
        }
        return mv;
    }
}
