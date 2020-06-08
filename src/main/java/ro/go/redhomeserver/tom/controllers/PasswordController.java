package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.exceptions.*;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.services.AuthService;
import ro.go.redhomeserver.tom.services.PasswordService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class PasswordController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordService passwordService;

    @GetMapping("/resetPassword")
    public String niceTry() {
        return "redirect:/auth";
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@RequestParam("username") String username, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("auth");
        mv.addObject("upperNotification", "Check your email address!");
        try {
            Account acc = authService.findAccountByUsername(username);
            String hostLink = request.getScheme() + "://" + request.getServerName();
            passwordService.makeResetRequest(acc, hostLink);
        } catch (UserNotFoundException e) {
            mv.addObject("upperNotification", "User Not Found!");
        } catch (SystemException e) {
            mv.addObject("upperNotification", "There was a problem in sending the reset email!");
        }
        return mv;
    }


    @GetMapping("/validateReset")
    public RedirectView validateReset(@RequestParam("token") String token, RedirectAttributes ra) {
        RedirectView rv;
        try {
            rv = new RedirectView("/setPassword");
            int id = passwordService.identifyAccount(token);
            ra.addFlashAttribute("userId", id);
        } catch (InvalidTokenException e) {
            rv = new RedirectView("/auth");
            ra.addFlashAttribute("upperNotification", "The token expired or is invalid!");
        }
        return rv;
    }

    @GetMapping("/setPassword")
    public ModelAndView loadResetRequest(@ModelAttribute("userId") int id) {
        ModelAndView mv = new ModelAndView("resetPassword");
        mv.addObject("userId", id);
        return mv;
    }

    @PostMapping("/setPassword")
    public ModelAndView handleResetRequest(@RequestParam Map<String, String> data, @ModelAttribute("userId") int id, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("resetPassword");
        mv.addObject("userId", id);
        try {
            passwordService.validatePassword(data.get("password"), data.get("passwordVerification"));
            passwordService.updateAccountPasswordById(Integer.parseInt(data.get("userId")), data.get("password"));
            mv = new ModelAndView("redirect:/auth");
            ra.addFlashAttribute("upperNotification", "Password updated!");
            return mv;
        } catch (WeakPasswordException e) {
            mv.addObject("error", "The password is too weak!");
        } catch (PasswordMismatchException e) {
            mv.addObject("error", "The password does not match!");
        } catch (SignUpException | SystemException e) {
            mv.addObject("error", "We're having some system issues! Try again later!");
        }
        return mv;
    }
}
