package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.exceptions.*;
import ro.go.redhomeserver.tom.services.PasswordService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/reset-password")
    public RedirectView resetPassword(@RequestParam("username") String username, HttpServletRequest request, RedirectAttributes ra) {
        RedirectView rv = new RedirectView("/tom/log-in");
        ra.addFlashAttribute("upperNotification", "Check your email address!");
        try {
            String hostLink = request.getScheme() + "://" + request.getServerName()+":8181/tom";
            passwordService.addResetRequest(username, hostLink);
        } catch (UserNotFoundException | SystemException e) {
            ra.addFlashAttribute("upperNotification", e.getMessage());
        }
        return rv;
    }

    @GetMapping("/validate-password-reset-request")
    public RedirectView validatePasswordResetRequest(@RequestParam("token") String token, RedirectAttributes ra) {
        RedirectView rv;
        try {
            rv = new RedirectView("/tom/set-new-password");
            String id = passwordService.identifyAccountUsingToken(token);
            ra.addFlashAttribute("userId", id);
        } catch (InvalidTokenException e) {
            rv = new RedirectView("/tom/log-in");
            ra.addFlashAttribute("upperNotification", e.getMessage());
        }
        return rv;
    }

    @GetMapping("/set-new-password")
    public ModelAndView setNewPassword(@ModelAttribute("userId") String id) {
        ModelAndView mv = new ModelAndView("reset-password");
        mv.addObject("userId", id);
        return mv;
    }

    @PostMapping("/set-new-password")
    public ModelAndView setNewPassword(@RequestParam Map<String, String> data, @ModelAttribute("userId") String id, RedirectAttributes ra, Authentication authentication) {
        ModelAndView mv = new ModelAndView("reset-password");
        mv.addObject("userId", id);

        if (authentication != null) {
            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
            updatedAuthorities.add(new SimpleGrantedAuthority("ACTIVATED"));
            Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

        if (!passwordService.isUserActivated(id))
            passwordService.activateMyAccount(id);

        try {
            passwordService.validatePassword(data.get("password"), data.get("passwordVerification"));
            passwordService.updateAccountPasswordById(data.get("userId"), data.get("password"));
            mv = new ModelAndView("redirect:/log-in");
            ra.addFlashAttribute("upperNotification", "Password updated!");
            return mv;
        } catch (SignUpException e) {
            mv.addObject("error", e.getMessage());
        }
        return mv;
    }
}
