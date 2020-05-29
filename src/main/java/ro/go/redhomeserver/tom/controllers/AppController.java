package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.go.redhomeserver.tom.services.AppRefreshService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tomapp")
public class AppController {

    @Autowired
    private AppRefreshService appRefreshService;

    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("index");
        appRefreshService.refreshData();
        if(request.getSession().getAttribute("active")==null) {
            mv = new ModelAndView("redirect:/auth");
            return mv;
        }

        return mv;
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



}