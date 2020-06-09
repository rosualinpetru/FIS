package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.go.redhomeserver.tom.exceptions.*;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.services.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/auth")
    public String auth(HttpServletRequest request) {
        if(request.getSession().getAttribute("active")!=null)
            return "redirect:/";

        return "log-in";
    }

    @PostMapping("/auth")
    public ModelAndView authenticate(@RequestParam Map<String, String> authData, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("log-in");
        mv.addObject("user", authData.get("username"));
        try {
            Account acc = authService.findAccountByUsername(authData.get("username"));
            authService.checkCredentials(acc, authData.get("password"));
            request.getSession().setAttribute("active", acc);
            return new ModelAndView("redirect:/");
        } catch (UserNotFoundException e) {
            mv.addObject("error", "User not found!");
        } catch (PasswordMatchException e) {
            mv.addObject("error", "Wrong password!");
        } catch (SystemException e) {
            mv.addObject("error", "We're having some system issues! Try again later!");
        }
        return mv;
    }

}
