package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/checkCredentials")
    public String checkCredentials(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request) {
        if(!username.equals("") && !password.equals(""))
        {
            try {
                Account acc = accountService.checkCredentials(username, password);
                sessionService.addAccountToSession(acc, request);
                return "index";
            }
            catch (Exception e) {
            }
        }
        return "auth";
    }
}
