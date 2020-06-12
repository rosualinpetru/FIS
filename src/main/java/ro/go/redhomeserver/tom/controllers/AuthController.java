package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.services.ClearDataService;
import ro.go.redhomeserver.tom.services.EmployeeService;
import ro.go.redhomeserver.tom.services.PasswordService;

@Controller
public class AuthController {

    private final ClearDataService clearDataService;
    private final PasswordService passwordService;
    private final EmployeeService employeeService;

    @Autowired
    public AuthController(ClearDataService clearDataService, PasswordService passwordService, EmployeeService employeeService) {
        this.clearDataService = clearDataService;
        this.passwordService = passwordService;
        this.employeeService = employeeService;
    }

    public boolean isUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/")
    public ModelAndView index(Model model, Authentication authentication) {
        clearDataService.clearData();
        ModelAndView mv = new ModelAndView("index");
        if (model.getAttribute("upperNotification") == null)
            mv.addObject("upperNotification", "");
        mv.addObject("iAmTeamLeader", employeeService.isTeamLeader(authentication.getName()));
        mv.addObject("employee", employeeService.findEmployeeByUsername(authentication.getName()));
        return mv;
    }

    @GetMapping("/log-in")
    public ModelAndView logIn(Model model) {
        if (isUserAuthenticated())
            return new ModelAndView("redirect:/");
        ModelAndView mv = new ModelAndView("log-in");
        if (model.getAttribute("upperNotification") == null)
            mv.addObject("upperNotification", "");
        return mv;
    }

    @GetMapping("/get-salt")
    @ResponseBody
    public String getSaltOfUser(@RequestParam("username") String username) {
        return passwordService.getSaltOfUser(username);
    }
}