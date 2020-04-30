package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ro.go.redhomeserver.tom.services.AppRefreshService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AppController {

    @Autowired
    private AppRefreshService appRefreshService;

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        appRefreshService.refreshData();
        if(request.getSession().getAttribute("active")==null)
            return "redirect:/auth";

        return "index";
    }

    @GetMapping("/auth")
    public String auth(HttpServletRequest request) {
        if(request.getSession().getAttribute("active")!=null)
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