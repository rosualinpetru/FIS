package ro.go.redhomeserver.tom.controllers;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.go.redhomeserver.tom.dtos.WebEvent;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayReq;
import ro.go.redhomeserver.tom.services.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;


    @GetMapping("/holidayReq")
    public ModelAndView holidayReq(HttpServletRequest request, RedirectAttributes ra) {
        ModelAndView mv = new ModelAndView("holidayReq");
        if (request.getSession().getAttribute("active") == null) {
            mv = new ModelAndView("redirect:/auth");
            ra.addFlashAttribute("upperNotification", "Please log in again (Session expired)!");
            return mv;
        }

        mv.addObject("delegates", employeeService.loadDelegates(((Account) request.getSession().getAttribute("active"))));

        return mv;
    }

    @PostMapping("/holidayReq")
    public ModelAndView resolveHolidayReq(@RequestParam Map<String, String> params, HttpServletRequest request, RedirectAttributes ra) {

        ModelAndView mv;
        if (request.getSession().getAttribute("active") == null) {
            mv = new ModelAndView("redirect:/auth");
            ra.addFlashAttribute("upperNotification", "Please log in again (Session expired)!");
            return mv;
        }

        employeeService.addRequestRecord((Account) request.getSession().getAttribute("active"), params);
        mv = new ModelAndView("redirect:/");

        return mv;

    }

    @GetMapping("/pendingRequest")
    public ModelAndView pendingRequest(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("approveHolReq");
        mv.addObject("pendingReq",employeeService.loadPendingReqByTl((Account) request.getSession().getAttribute("active")));
        return mv;
    }

    @GetMapping("/loadMyTeamsSchedule")
    @ResponseBody
    public List<WebEvent> loadHolidayReq(HttpServletRequest request) {
        return employeeService.loadHolidayReqByTl((Account) request.getSession().getAttribute("active"));
    }

    @PostMapping("/acceptReq")
    @ResponseBody
    public void updateStatusReq(@RequestParam String id,@RequestParam String act) {
        employeeService.updateStatusReq(Integer.parseInt(id),act);
    }

}
