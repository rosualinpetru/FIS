package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.services.ITService;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.SystemException;
import java.util.Map;


@Controller
public class ITController {
    private final ITService itService;

    @Autowired
    public ITController(ITService itService) {
        this.itService = itService;
    }

    @GetMapping("/reportIssue")
    public ModelAndView reportIssue(HttpServletRequest request, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("reportIssue");
        Account acc = (Account) request.getSession().getAttribute("active");
        if (acc == null) {
            mv = new ModelAndView("redirect:/auth");
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
            mv = new ModelAndView("redirect:/auth");
            ra.addFlashAttribute("upperNotification", "Please log in again (Session expired)!");
            return mv;

        }
        itService.addIssueRequest(params);
        ra.addFlashAttribute("upperNotification", "Issue reported!");
        return mv;
    }


    @GetMapping("/createAccount")
    public String createAccount(@ModelAttribute("emplId") int id_empl, @ModelAttribute("tlId") int id_tl, RedirectAttributes ra) {
        try {
            itService.generateAccount(id_empl, id_tl);
        } catch (SystemException e) {
            itService.informItAboutSystemError(id_empl);
        }
        ra.addFlashAttribute("upperNotification", "The employee record was added!");
        return "redirect:/";

    }


    @GetMapping("/pendingIssue")
    public ModelAndView pendingIssue() {

        ModelAndView mv = new ModelAndView("pendingIssue");
        mv.addObject("ListPendingIssue", itService.loadAllPendingIssueRequests());
        return mv;


    }

    @ResponseBody
    @PostMapping("/deleteIssue")
    public void deteleIssue(@RequestParam("id") String id) {
        itService.deleteIssueRequestById(Integer.parseInt(id));


    }

    @GetMapping("/manageDepartment")

    public ModelAndView manageDepartment() {

        ModelAndView mv = new ModelAndView("manageDepartment");
        mv.addObject("departments", itService.loadDepartments());
        return mv;
    }

    @PostMapping("/deleteDepartment")
    public ModelAndView deleteDepartment(@RequestParam("departmentId") String id) {
        itService.removeDepartment(Integer.parseInt(id));
        return new ModelAndView("redirect:/manageDepartment");
    }

    @PostMapping("/addDepartment")
    public ModelAndView addDepartment(@RequestParam("departmentName") String name) {
        itService.addDepartment(name);
        return new ModelAndView("redirect:/manageDepartment");
    }

    @GetMapping("/manageEmployee")
    public ModelAndView manageEmployee() {
        ModelAndView mv = new ModelAndView("deleteEmployee");
        mv.addObject("departments", itService.loadDepartments());
        return mv;
    }

    @PostMapping("/deleteEmployee")
    public ModelAndView deleteEmployee(@RequestParam("emplID") String id) {
        itService.removeEmployee(Integer.parseInt(id));
        return new ModelAndView("redirect:/manageEmployee");
    }

    @GetMapping("/changeTeamLeader")
    public ModelAndView changeTL() {
        ModelAndView mv = new ModelAndView("changeTL");
        mv.addObject("departments", itService.loadDepartments());
        return mv;
    }

    @PostMapping("/changeTL")
    public ModelAndView changeTLEmpl(@RequestParam("emplID") String id, @RequestParam("TLID") String id2) {
        itService.updateTeamLeader(Integer.parseInt(id), Integer.parseInt(id2));
        return new ModelAndView("redirect:/changeTL");
    }

}