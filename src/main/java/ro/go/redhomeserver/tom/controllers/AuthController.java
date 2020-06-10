package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ro.go.redhomeserver.tom.services.ClearDataService;

@Controller
public class AuthController {

    private final ClearDataService clearDataService;

    @Autowired
    public AuthController(ClearDataService clearDataService) {
        this.clearDataService = clearDataService;
    }

    public boolean isUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/log-in")
    public ModelAndView logInGet() {
        if (isUserAuthenticated())
            return new ModelAndView("redirect:/");
        return new ModelAndView("log-in");
    }

    @GetMapping("/")
    public ModelAndView index() {
        clearDataService.clearData();
        return new ModelAndView("index");
    }
}