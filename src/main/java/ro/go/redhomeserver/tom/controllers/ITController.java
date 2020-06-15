package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.services.EmployeeService;
import ro.go.redhomeserver.tom.services.FormService;
import ro.go.redhomeserver.tom.services.ITService;


@Controller
public class ITController {

    private final ITService itService;
    private final FormService formService;
    private final EmployeeService employeeService;

    @Autowired
    public ITController(ITService itService, FormService formService, EmployeeService employeeService) {
        this.itService = itService;
        this.formService = formService;
        this.employeeService = employeeService;
    }

    @GetMapping("/manage-department")
    public ModelAndView manageDepartment() {
        ModelAndView mv = new ModelAndView("manage-department");
        mv.addObject("departments", formService.loadDepartments());
        return mv;
    }

    @PostMapping("/add-department")
    public RedirectView addDepartment(@RequestParam("departmentName") String departmentName) {
        itService.addDepartment(departmentName);
        return new RedirectView("/tom/manage-department");
    }

    @PostMapping("/delete-department")
    public RedirectView deleteDepartment(@RequestParam("departmentId") String departmentId) {
        itService.removeDepartment(departmentId);
        return new RedirectView("/tom/manage-department");
    }

    @GetMapping("/delete-employee")
    public ModelAndView manageEmployee() {
        ModelAndView mv = new ModelAndView("delete-employee");
        mv.addObject("departments", formService.loadDepartments());
        return mv;
    }

    @PostMapping("/delete-employee")
    public RedirectView deleteEmployee(@RequestParam("employeeId") String employeeId) {
        employeeService.removeEmployee(employeeId);
        return new RedirectView("/tom/delete-employee");
    }

    @GetMapping("/change-team-leader")
    public ModelAndView changeTeamLeader() {
        ModelAndView mv = new ModelAndView("change-team-leader");
        mv.addObject("departments", formService.loadDepartments());
        return mv;
    }

    @PostMapping("/change-team-leader")
    public RedirectView changeTeamLeader(@RequestParam("employeeId") String employeeId, @RequestParam("teamLeaderId") String teamLeaderId) {
        employeeService.updateTeamLeader(employeeId, teamLeaderId);
        return new RedirectView("/tom/change-team-leader");
    }
}