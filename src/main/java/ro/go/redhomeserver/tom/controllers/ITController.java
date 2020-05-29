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
@RequestMapping("/tomapp")
public class ITController {


    @Autowired
    private ITService itService;



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
        itService.reportIssueWithData(params);
        ra.addFlashAttribute("upperNotification", "Issue reported!");
        return mv;
    }



    @GetMapping("/createAccount")
    public String createAccount(@ModelAttribute("emplId") int id_empl, @ModelAttribute("tlId") int id_tl, RedirectAttributes ra) {
        try {
            itService.generateAccount(id_empl, id_tl);
        }catch (SystemException e){
            itService.informItAboutError(id_empl);
        }
        ra.addFlashAttribute("upperNotification", "The employee record was added!");
        return "redirect:/";

    }


}