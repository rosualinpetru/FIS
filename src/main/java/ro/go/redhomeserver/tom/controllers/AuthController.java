package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ro.go.redhomeserver.tom.services.ClearDataService;
import ro.go.redhomeserver.tom.services.PasswordService;

@Controller
public class AuthController {

    private final ClearDataService clearDataService;
    private final PasswordService passwordService;
    @Autowired
    public AuthController(ClearDataService clearDataService, PasswordService passwordService) {
        this.clearDataService = clearDataService;
        this.passwordService = passwordService;
    }

    public boolean isUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/")
    public ModelAndView index() {
        clearDataService.clearData();
        return new ModelAndView("index");
    }

    @GetMapping("/log-in")
    public ModelAndView logIn() {
        ModelAndView mv = new  ModelAndView("log-in");
        mv.addObject("upperNotification", "");
        if (isUserAuthenticated())
            return new ModelAndView("redirect:/");
        return mv;
    }

    @GetMapping("/get-salt")
    @ResponseBody
    public String getSaltOfUser(@RequestParam("username") String username) {
        return passwordService.getSaltOfUser(username);
    }
}