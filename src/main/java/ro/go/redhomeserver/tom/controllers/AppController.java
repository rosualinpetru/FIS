package ro.go.redhomeserver.tom.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "AuthPage");
        return "index";
    }

    @GetMapping("/auth")
    public String auth() {
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