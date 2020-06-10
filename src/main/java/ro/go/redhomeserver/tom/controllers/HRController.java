package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.go.redhomeserver.tom.exceptions.UsedEmailException;
import ro.go.redhomeserver.tom.exceptions.SignUpException;
import ro.go.redhomeserver.tom.services.DepartmentService;
import ro.go.redhomeserver.tom.services.HRService;

import java.util.Map;

@Controller
public class HRController {

    private final HRService hrService;
    private final DepartmentService departmentService;

    @Autowired
    public HRController(HRService hrService, DepartmentService departmentService) {
        this.hrService = hrService;
        this.departmentService = departmentService;
    }

    @GetMapping("/sign-up")
    public ModelAndView signUp() {
        ModelAndView mv = new ModelAndView("sign-up");
        mv.addObject("departments", departmentService.loadDepartments());
        mv.addObject("error", "");
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
        } catch (UsedEmailException e) {
            mv.addObject("departments", departmentService.loadDepartments());
            mv.addObject("error", "The email is already used!");
        } catch (SignUpException e) {
            mv.addObject("departments", departmentService.loadDepartments());
            mv.addObject("error", "An error has occurred!");
        }
        return mv;
    }
}
