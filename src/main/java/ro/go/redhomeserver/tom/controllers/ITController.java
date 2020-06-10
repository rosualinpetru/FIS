package ro.go.redhomeserver.tom.controllers;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.services.DepartmentService;
import ro.go.redhomeserver.tom.services.EmployeeService;
import ro.go.redhomeserver.tom.services.ITService;
import ro.go.redhomeserver.tom.services.IssueRequestService;

import javax.transaction.SystemException;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class ITController {
    private final ITService itService;
    private final IssueRequestService issueRequestService;
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @Autowired
    public ITController(ITService itService, IssueRequestService issueRequestService, DepartmentService departmentService, EmployeeService employeeService) {
        this.itService = itService;
        this.issueRequestService = issueRequestService;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @GetMapping("/create-account")
    public RedirectView createAccount(@ModelAttribute("employeeId") int employeeId, @ModelAttribute("teamLeaderId") int teamLeaderId, RedirectAttributes ra) {
        RedirectView rv = new RedirectView("/");
        try {
            itService.generateAccount(employeeId, teamLeaderId);
        } catch (SystemException e) {
            itService.informItAboutSystemError(employeeId);
        }
        ra.addFlashAttribute("upperNotification", "The employee record was added!");
        return rv;
    }

    @GetMapping("/pending-issues")
    public ModelAndView pendingIssues() {
        ModelAndView mv = new ModelAndView("pending-issues");
        mv.addObject("pendingIssues", issueRequestService.loadAllPendingIssueRequests());
        return mv;
    }

    @PostMapping("/delete-issue")
    @ResponseBody
    public void deleteIssue(@RequestParam("issueId") String issueId) {
       issueRequestService.deleteIssueRequestById(Integer.parseInt(issueId));
    }

    @GetMapping("/manage-departments")
    public ModelAndView manageDepartment() {
        ModelAndView mv = new ModelAndView("manage-department");
        mv.addObject("departments", departmentService.loadDepartments());
        return mv;
    }

    @PostMapping("/add-department")
    public RedirectView addDepartment(@RequestParam("departmentName") String departmentName) {
        departmentService.addDepartment(departmentName);
        return new RedirectView("/manage-department");
    }

    @PostMapping("/delete-department")
    public RedirectView deleteDepartment(@RequestParam("departmentId") String departmentId) {
        departmentService.removeDepartment(Integer.parseInt(departmentId));
        return new RedirectView("/manage-department");
    }

    @GetMapping("/delete-employee")
    public ModelAndView manageEmployee() {
        ModelAndView mv = new ModelAndView("delete-employee");
        mv.addObject("departments", departmentService.loadDepartments());
        return mv;
    }

    @PostMapping("/delete-employee")
    public RedirectView deleteEmployee(@RequestParam("employeeId") String employeeId) {
        employeeService.removeEmployee(Integer.parseInt(employeeId));
        return new RedirectView("/delete-employee");
    }

    @GetMapping("/change-team-leader")
    public ModelAndView changeTeamLeader() {
        ModelAndView mv = new ModelAndView("change-team-leader");
        mv.addObject("departments", departmentService.loadDepartments());
        return mv;
    }

    @PostMapping("/change-team-leader")
    public RedirectView changeTeamLeader(@RequestParam("employeeId") String employeeId, @RequestParam("teamLeaderId") String teamLeaderId) {
        employeeService.updateTeamLeader(Integer.parseInt(employeeId), Integer.parseInt(teamLeaderId));
        return new RedirectView("/change-team-leader");
    }

    @GetMapping("/update-delete-employee-form")
    @ResponseBody
    public List<Pair<Integer, String>> getEmployeesOfDepartment(@RequestParam("departmentId") int departmentId, Authentication authentication) {
        List<Employee> allOfDepartmentButMe = departmentService.loadEmployeesOfDepartmentById(departmentId);
        allOfDepartmentButMe.remove(employeeService.findEmployeeByUsername(authentication.getName()));
        return allOfDepartmentButMe.stream().map(s -> new Pair<>(s.getId(), s.getName())).collect(Collectors.toList());
    }
}