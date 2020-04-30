package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/auth")
    public ModelAndView authenticate(@RequestParam Map<String, String> authData, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("auth");
        mv.addObject("user", authData.get("username"));
        try {
            authService.validateData(authData.get("username"), authData.get("password"));
            Account acc = authService.findAccountByUsername(authData.get("username"));
            authService.checkCredentials(acc, authData.get("password"));
            request.getSession().setAttribute("active", acc);
            return new ModelAndView("redirect:/");
        } catch (EmptyFiledException e) {
            mv.addObject("error", "Some fields are empty!");
        } catch (UserNotFoundException e) {
            mv.addObject("error", "User not found!");
        } catch (PasswordMatchException e) {
            mv.addObject("error", "Wrong password!");
        } catch (SystemException e) {
            mv.addObject("error", "We're having some system issues! Try again later!");
        }
        return mv;
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@RequestParam("username") String username, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("auth");
        mv.addObject("sentEmail", "Check your email address!");
        try {
            Account acc = authService.findAccountByUsername(username);
            String hostLink = request.getScheme() + "://" + request.getServerName();
            authService.makeResetRequest(acc, hostLink);
        } catch (LogInException e) {
            mv.addObject("sentEmail", "User Not Found!");
        }
        return mv;
    }
}
