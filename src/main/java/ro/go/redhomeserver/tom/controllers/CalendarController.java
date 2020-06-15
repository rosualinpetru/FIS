package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.services.CalendarService;

import java.util.List;

@Controller
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/team-schedule")
    @ResponseBody
    public List<CalendarEvent> teamSchedule(Authentication authentication) {
        try {
            return calendarService.loadHolidayRequestsOfTeamLeaderForCalendar(authentication.getName());
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    @GetMapping("/company-calendar")
    @ResponseBody
    public List<CalendarEvent> companyCalendar(@RequestParam("id") String accountId) {
        try {
            return calendarService.loadHolidayRequestsOfTeamLeaderForCalendarById(accountId);
        } catch (UserNotFoundException e) {
            return null;
        }
    }
}
