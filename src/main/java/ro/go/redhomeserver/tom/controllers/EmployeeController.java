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
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.services.EmployeeService;
import ro.go.redhomeserver.tom.services.IssueRequestService;

import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final IssueRequestService issueRequestService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, IssueRequestService issueRequestService) {
        this.employeeService = employeeService;
        this.issueRequestService = issueRequestService;
    }

    @GetMapping("/request-holiday")
    public ModelAndView requestHoliday(Authentication authentication) {
        ModelAndView mv = new ModelAndView("request-holiday");
        mv.addObject("delegates", employeeService.loadPossibleDelegates(authentication.getName()));
        return mv;
    }

    @PostMapping("/request-holiday")
    public RedirectView requestHoliday(@RequestParam Map<String, String> params, Authentication authentication, RedirectAttributes ra) {
        RedirectView rv = new RedirectView("/");
        try {
            employeeService.addHolidayRequest(authentication.getName(), params);
            ra.addFlashAttribute("upperNotification", "Your request was sent to your team leader!");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("upperNotification", "There was an error in the system! The request was not sent!");
        }
        return rv;
    }

    @GetMapping("/report-issue")
    public ModelAndView reportIssue() {
        return new ModelAndView("report-issue");
    }

    @PostMapping("/report-issue")
    public RedirectView reportIssue(@RequestParam Map<String, String> params, Authentication authentication, RedirectAttributes ra) {
        RedirectView rv = new RedirectView("/");
        try {
            issueRequestService.addIssueRequest(authentication.getName(), params);
            ra.addFlashAttribute("upperNotification", "Issue reported!");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("upperNotification", "There was an error in the system! The request was not sent!");
        }
        return rv;
    }

    @GetMapping("/pending-holiday-requests")
    public ModelAndView pendingHolidayRequests(Authentication authentication) {
        ModelAndView mv = new ModelAndView("pending-holiday-requests");
        mv.addObject("pendingHolidayRequests", employeeService.loadPendingHolidayRequestsForATeamLeader(authentication.getName()));
        return mv;
    }

    @GetMapping("/team-schedule")
    @ResponseBody
    public List<CalendarEvent> teamSchedule(Authentication authentication) {
        return employeeService.loadHolidayRequestsOfTeamLeaderForCalendar(authentication.getName());
    }

    @PostMapping("/update-holiday-request")
    @ResponseBody
    public void updateHolidayRequest(@RequestParam String holidayRequestId, @RequestParam String action) {
        employeeService.updateStatusOfHolidayRequest(Integer.parseInt(holidayRequestId), action);
    }
}
