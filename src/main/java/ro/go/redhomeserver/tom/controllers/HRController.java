package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.exceptions.SignUpException;
import ro.go.redhomeserver.tom.services.FormService;
import ro.go.redhomeserver.tom.services.HRService;

import java.util.Map;

@Controller
public class HRController {

    private final HRService hrService;
    private final FormService formService;

    @Autowired
    public HRController(HRService hrService, FormService formService) {
        this.hrService = hrService;
        this.formService = formService;
    }

    @GetMapping("/sign-up")
    public ModelAndView signUp() {
        ModelAndView mv = new ModelAndView("sign-up");
        mv.addObject("departments", formService.loadDepartments());
        mv.addObject("error", null);
        return mv;
    }

    @PostMapping("/sign-up")
    public ModelAndView signUp(@RequestParam Map<String, String> params, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("sign-up");
        try {
            hrService.checkIfEmailIsAvailable(params);
            mv = new ModelAndView("redirect:/create-account");
            ra.addFlashAttribute("employeeId", hrService.addEmployee(params));
            ra.addFlashAttribute("teamLeaderId", params.get("teamLeaderId"));
        } catch (SignUpException e) {
            mv.addObject("departments", formService.loadDepartments());
            mv.addObject("error", e.getMessage());
        }
        return mv;
    }

    @GetMapping("/company-schedule")
    public ModelAndView companySchedule() {
        ModelAndView mv = new ModelAndView("company-schedule");
        mv.addObject("departments", formService.loadDepartments());
        return mv;
    }

    @GetMapping("/company-requests-feedback")
    public ModelAndView pendingHolidayRequestsHr(@RequestParam(name = "departmentId", required = false) String departmentId) {
        ModelAndView mv = new ModelAndView("company-requests-feedback");
        mv.addObject("requests", hrService.loadRequestsOfDepartment(departmentId));
        mv.addObject("selectedDepartment", departmentId);
        mv.addObject("departments", formService.loadDepartments());
        return mv;
    }

    @PostMapping("feedback")
    public RedirectView feedback(@RequestParam Map<String, String> params, Authentication authentication) {
        hrService.addFeedback(params.get("requestId"), params.get("description"), authentication.getName());
        return new RedirectView("/tom/company-requests-feedback");
    }
}
