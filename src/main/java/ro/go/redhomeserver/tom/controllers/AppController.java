package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.go.redhomeserver.tom.services.ClearDataService;
import javax.servlet.http.HttpServletRequest;

@Controller
public class AppController {

    private final ClearDataService clearDataService;

    @Autowired
    public AppController(ClearDataService clearDataService) {
        this.clearDataService = clearDataService;
    }

    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request, RedirectAttributes ra) {
        // Update database
        clearDataService.clearData();
        if (request.getSession().getAttribute("active") == null)
            return new ModelAndView("log-in");
        return new ModelAndView("index");
    }
}