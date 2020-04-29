package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.go.redhomeserver.tom.exceptions.LogInException;
import ro.go.redhomeserver.tom.exceptions.PasswordMatchException;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.services.AccountService;
import ro.go.redhomeserver.tom.services.SessionService;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/auth")
    public String checkCredentials(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpServletRequest request) {
        model.addAttribute("user", username);
        if (!username.equals("") && !password.equals("")) {
            model.addAttribute("error", "");
            try {
                Account acc = accountService.checkCredentials(username, password);
                sessionService.addAccountSession(acc, request);
                return "redirect:/";
            } catch (UserNotFoundException e) {
                model.addAttribute("error", "User not found!");
            } catch (PasswordMatchException e) {
                model.addAttribute("error", "Wrong password!");
            } catch (SystemException | LogInException e) {
                model.addAttribute("error", "We're having some system issues! Try again later!");
            }
        } else {
            model.addAttribute("error", "Please complete all fields!");
        }
        return "auth";
    }
}
