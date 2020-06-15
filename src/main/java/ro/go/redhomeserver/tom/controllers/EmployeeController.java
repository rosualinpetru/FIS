package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.exceptions.FileStorageException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.UploadedFile;
import ro.go.redhomeserver.tom.services.EmployeeService;
import ro.go.redhomeserver.tom.services.IssueRequestService;
import ro.go.redhomeserver.tom.services.UploadedFileService;

import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final IssueRequestService issueRequestService;
    private final UploadedFileService uploadedFileService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, IssueRequestService issueRequestService, UploadedFileService uploadedFileService) {
        this.employeeService = employeeService;
        this.issueRequestService = issueRequestService;
        this.uploadedFileService = uploadedFileService;
    }

    @GetMapping("/request-holiday")
    public ModelAndView requestHoliday(Authentication authentication) {
        ModelAndView mv = new ModelAndView("request-holiday");
        try {
            mv.addObject("delegates", employeeService.loadPossibleDelegates(authentication.getName()));
        } catch (UserNotFoundException e) {
            mv.addObject("delegates", null);
        }
        return mv;
    }

    @PostMapping("/request-holiday")
    public RedirectView requestHoliday(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params, Authentication authentication, RedirectAttributes ra) {
        RedirectView rv = new RedirectView("/tom/");
        try {
            employeeService.addHolidayRequest(authentication.getName(), params, file);
            ra.addFlashAttribute("upperNotification", "Your request was sent to your team leader!");
        } catch (FileStorageException e) {
            ra.addFlashAttribute("upperNotification", e.getMessage());
        }
        return rv;
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

    @GetMapping("/pending-holiday-requests")
    public ModelAndView pendingHolidayRequests(Authentication authentication) {
        ModelAndView mv = new ModelAndView("pending-holiday-requests");
        try {
            mv.addObject("pendingHolidayRequests", employeeService.loadPendingHolidayRequestsForATeamLeader(authentication.getName()));
        } catch (UserNotFoundException e) {
            mv.addObject("pendingHolidayRequests", null);
        }
        return mv;
    }

    @GetMapping("/team-schedule")
    @ResponseBody
    public List<CalendarEvent> teamSchedule(Authentication authentication) {
        try {
            return employeeService.loadHolidayRequestsOfTeamLeaderForCalendar(authentication.getName());
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    @PostMapping("/update-holiday-request")
    @ResponseBody
    public void updateHolidayRequest(@RequestParam("id") String holidayRequestId, @RequestParam("act") String action) {
        employeeService.updateStatusOfHolidayRequest(holidayRequestId, action);
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("holidayRequestId") String holidayRequestId) {
        UploadedFile databaseFile = uploadedFileService.getFileByRequest(holidayRequestId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + ".pdf\"")
                .body(new ByteArrayResource(databaseFile.getData()));
    }
}
