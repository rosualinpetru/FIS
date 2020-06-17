package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.services.IssueRequestService;

import java.util.Map;

@Controller
public class IssueController {

    private final IssueRequestService issueRequestService;

    @Autowired
    public IssueController(IssueRequestService issueRequestService) {
        this.issueRequestService = issueRequestService;
    }

    @GetMapping("/report-issue")
    public ModelAndView reportIssue() {
        return new ModelAndView("report-issue");
    }

    @PostMapping("/report-issue")
    public RedirectView reportIssue(@RequestParam Map<String, String> params, Authentication authentication, RedirectAttributes ra) {
        RedirectView rv = new RedirectView("/tom/");
        try {
            issueRequestService.addIssueRequest(authentication.getName(), params);
            ra.addFlashAttribute("upperNotification", "Issue reported!");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("upperNotification", "There was an error in the system! The request was not sent!");
        }
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
        issueRequestService.deleteIssueRequestById(issueId);
    }

}
