package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.services.AuthenticationService;
import ro.go.redhomeserver.tom.services.ClearDataService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class AuthenticationController {

    private final ClearDataService clearDataService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(ClearDataService clearDataService, AuthenticationService authenticationService) {
        this.clearDataService = clearDataService;
        this.authenticationService = authenticationService;
    }

    private boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/")
    public ModelAndView index(Model model, Authentication authentication, HttpServletResponse httpServletResponse) throws IOException {
        clearDataService.clearData();
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("upperNotification", model.getAttribute("upperNotification"));
        mv.addObject("isTeamLeader", authenticationService.amITeamLeader(authentication.getName()));
        try {
            mv.addObject("employee", authenticationService.getMyEmployeeData(authentication.getName()));
            return mv;
        } catch (UserNotFoundException e) {
            httpServletResponse.sendRedirect("/tom/log-out");
            return null;
        }
    }

    @GetMapping("/log-in")
    public ModelAndView logIn(Model model, Authentication authentication) {
        if (isUserAuthenticated(authentication))
            return new ModelAndView("redirect:/");
        ModelAndView mv = new ModelAndView("log-in");
        mv.addObject("upperNotification", model.getAttribute("upperNotification"));
        return mv;
    }

    @GetMapping("/get-salt")
    @ResponseBody
    public String saltUser(@RequestParam("username") String username) {
        return authenticationService.getSaltOfUser(username);
    }
}