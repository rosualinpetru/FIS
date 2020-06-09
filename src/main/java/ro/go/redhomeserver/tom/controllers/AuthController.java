package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.go.redhomeserver.tom.exceptions.*;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.services.LogInService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AuthController {

    private final LogInService logInService;

    @Autowired
    public AuthController(LogInService logInService) {
        this.logInService = logInService;
    }

    @GetMapping("/log-in")
    public ModelAndView logInGet(HttpServletRequest request) {
        if(request.getSession().getAttribute("active")!=null)
            return new ModelAndView("redirect:/");
        return new ModelAndView("log-in");
    }

    @PostMapping("/log-in")
    public ModelAndView logInPost(@RequestParam Map<String, String> loginData, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("log-in");
        try {
            Account acc = logInService.searchForUser(loginData.get("username"));
            logInService.checkCredentials(acc, loginData.get("password"));
            request.getSession().setAttribute("active", acc.getId());
            return new ModelAndView("redirect:/");
        } catch (UserNotFoundException e) {
            mv.addObject("error", "User not found!");
        } catch (PasswordMatchException e) {
            mv.addObject("error", "Wrong password!");
        } catch (SystemException e) {
            mv.addObject("error", "We're having some system issues! Try again later!");
        }
        mv.addObject("user", loginData.get("username"));
        return mv;
    }

}
