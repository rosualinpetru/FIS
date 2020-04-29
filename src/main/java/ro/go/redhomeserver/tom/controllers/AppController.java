package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ro.go.redhomeserver.tom.services.SessionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AppController {

    @Autowired
    private SessionService sessionService;

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        if(!sessionService.amILoggedIn(request))
            return "redirect:/auth";
        model.addAttribute("name", "AuthPage");
        return "index";
    }

    @GetMapping("/auth")
    public String auth(Model model, HttpServletRequest request) {
        if(sessionService.amILoggedIn(request))
            return "redirect:/";

        return "auth";
    }

    @GetMapping("/addEmployeeRecord")
    public String addEmployeeRecord() {
        return "addEmployeeRecord";
    }
    
    @GetMapping("/calendar")
    public String calendar() {
        return "calendar";
    }

    @GetMapping("/maintainApp")
    public String maintainApp() {
        return "maintainApp";
    }

    @GetMapping("/pendingReq")
    public String pendingReq() {
        return "pendingReq";
    }

    @GetMapping("/reqStatus")
    public String reqStatus() {
        return "reqStatus";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "signUp";
    }

    @GetMapping("/reportIssue")
    public String reportIssue() {
        return "reportIssue";
    }

}