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
import ro.go.redhomeserver.tom.exceptions.NotEnoughDaysException;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.UploadedFile;
import ro.go.redhomeserver.tom.services.FormService;
import ro.go.redhomeserver.tom.services.HolidayService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Controller
public class HolidayController {

    private final HolidayService holidayService;
    private final FormService formService;

    @Autowired
    public HolidayController(HolidayService holidayService, FormService formService) {
        this.holidayService = holidayService;
        this.formService = formService;
    }

    @GetMapping("/request-holiday")
    public ModelAndView requestHoliday(Authentication authentication) {
        ModelAndView mv = new ModelAndView("request-holiday");
        mv.addObject("error", null);
        try {
            mv.addObject("delegates", formService.loadPossibleDelegates(authentication.getName()));
        } catch (UserNotFoundException e) {
            mv.addObject("delegates", null);
        }
        return mv;
    }

    @PostMapping("/request-holiday")
    public ModelAndView requestHoliday(@RequestParam("file") MultipartFile file, @RequestParam Map<String, String> params, Authentication authentication, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("redirect:/");
        try {
            holidayService.addHolidayRequest(authentication.getName(), params, file);
            ra.addFlashAttribute("upperNotification", "Your request will be processed!");
        } catch (IOException | UserNotFoundException e) {
            ra.addFlashAttribute("upperNotification", e.getMessage());
        } catch (NotEnoughDaysException e) {
            mv = new ModelAndView("request-holiday");
            mv.addObject("error", e.getMessage());
        } catch (ParseException e) {
            mv = new ModelAndView("request-holiday");
            mv.addObject("error", "There was an error in the system! Try again later!");
        }
        return mv;
    }

    @GetMapping("/pending-holiday-requests")
    public ModelAndView pendingHolidayRequests(Authentication authentication) {
        ModelAndView mv = new ModelAndView("pending-holiday-requests");
        try {
            mv.addObject("pendingHolidayRequests", holidayService.loadPendingHolidayRequestsForATeamLeader(authentication.getName()));
        } catch (UserNotFoundException e) {
            mv.addObject("pendingHolidayRequests", null);
        }
        return mv;
    }

    @GetMapping("/my-requests-status")
    public ModelAndView myRequestsStatus(@RequestParam(name = "status", required = false) String status, Authentication authentication) {
        ModelAndView mv = new ModelAndView("my-requests-status");
        mv.addObject("selectedStatus",status);
        try {
            if (status == null || status.equals("Sent"))
                mv.addObject("requests", holidayService.loadMyPendingHolidayRequests(authentication.getName()));
            else {
                if (status.equals("Accepted"))
                    mv.addObject("requests", holidayService.loadMyAcceptedHolidayRequests(authentication.getName()));

                if (status.equals("Declined"))
                    mv.addObject("requests", holidayService.loadMyDeclinedHolidayRequests(authentication.getName()));
            }
        } catch (UserNotFoundException e) {
            mv.addObject("requests", null);
        }
        return mv;
    }

    @PostMapping("/update-holiday-request")
    @ResponseBody
    public void updateHolidayRequest(@RequestParam("id") String holidayRequestId, @RequestParam("act") String action, RedirectAttributes ra, HttpServletResponse response) throws IOException {
        try {
            holidayService.updateStatusOfHolidayRequest(holidayRequestId, action);
        } catch (SystemException e) {
            ra.addFlashAttribute("upperNotification", "Confirmation email was not sent!");
            response.sendRedirect("/tom/");
        }
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("holidayRequestId") String holidayRequestId) {
        UploadedFile databaseFile = holidayService.getFileOfRequest(holidayRequestId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(databaseFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + databaseFile.getFileName() + ".pdf\"")
                .body(new ByteArrayResource(databaseFile.getData()));
    }
}
