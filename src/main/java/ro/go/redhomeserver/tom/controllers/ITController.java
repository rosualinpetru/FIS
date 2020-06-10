package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.services.DepartmentService;
import ro.go.redhomeserver.tom.services.EmployeeService;
import ro.go.redhomeserver.tom.services.ITService;
import ro.go.redhomeserver.tom.services.IssueRequestService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.SystemException;
import java.util.Map;


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
    public String createAccount(@ModelAttribute("employeeId") int employeeId, @ModelAttribute("teamLeaderId") int teamLeaderId, RedirectAttributes ra) {
        try {
            itService.generateAccount(employeeId, teamLeaderId);
        } catch (SystemException e) {
            itService.informItAboutSystemError(employeeId);
        }
        ra.addFlashAttribute("upperNotification", "The employee record was added!");
        return "redirect:/";

    }

    @GetMapping("/reportIssue")
    public ModelAndView reportIssue(HttpServletRequest request, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("reportIssue");
        Account acc = (Account) request.getSession().getAttribute("active");
        if (acc == null) {
            mv = new ModelAndView("redirect:/log-in");
            ra.addFlashAttribute("upperNotification", "Please log in again (Session expired)!");
            return mv;
        }
        mv.addObject("myID", acc.getId());
        return mv;
    }

    @PostMapping("/reportIssue")
    public ModelAndView reportIssue(@RequestParam Map<String, String> params, RedirectAttributes ra, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("redirect:/");
        Account acc = (Account) request.getSession().getAttribute("active");
        if (acc == null) {
            mv = new ModelAndView("redirect:/log-in");
            ra.addFlashAttribute("upperNotification", "Please log in again (Session expired)!");
            return mv;

        }
        issueRequestService.addIssueRequest(params);
        ra.addFlashAttribute("upperNotification", "Issue reported!");
        return mv;
    }


    @GetMapping("/pendingIssue")
    public ModelAndView pendingIssue() {

        ModelAndView mv = new ModelAndView("pendingIssue");
        mv.addObject("ListPendingIssue", issueRequestService.loadAllPendingIssueRequests());
        return mv;


    }

    @ResponseBody
    @PostMapping("/deleteIssue")
    public void deteleIssue(@RequestParam("id") String id) {
       issueRequestService.deleteIssueRequestById(Integer.parseInt(id));


    }

    @GetMapping("/manageDepartment")

    public ModelAndView manageDepartment() {

        ModelAndView mv = new ModelAndView("manageDepartment");
        mv.addObject("departments", departmentService.loadDepartments());
        return mv;
    }

    @PostMapping("/deleteDepartment")
    public ModelAndView deleteDepartment(@RequestParam("departmentId") String id) {
        departmentService.removeDepartment(Integer.parseInt(id));
        return new ModelAndView("redirect:/manageDepartment");
    }

    @PostMapping("/addDepartment")
    public ModelAndView addDepartment(@RequestParam("departmentName") String name) {
        departmentService.addDepartment(name);
        return new ModelAndView("redirect:/manageDepartment");
    }

    @GetMapping("/manageEmployee")
    public ModelAndView manageEmployee() {
        ModelAndView mv = new ModelAndView("deleteEmployee");
        mv.addObject("departments", departmentService.loadDepartments());
        return mv;
    }

    @PostMapping("/deleteEmployee")
    public ModelAndView deleteEmployee(@RequestParam("emplID") String id) {
        employeeService.removeEmployee(Integer.parseInt(id));
        return new ModelAndView("redirect:/manageEmployee");
    }

    @GetMapping("/changeTeamLeader")
    public ModelAndView changeTL() {
        ModelAndView mv = new ModelAndView("changeTL");
        mv.addObject("departments", departmentService.loadDepartments());
        return mv;
    }

    @PostMapping("/changeTL")
    public ModelAndView changeTLEmpl(@RequestParam("emplID") String id, @RequestParam("TLID") String id2) {
        employeeService.updateTeamLeader(Integer.parseInt(id), Integer.parseInt(id2));
        return new ModelAndView("redirect:/changeTL");
    }

}