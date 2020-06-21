package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.services.AccountService;

@Controller
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/create-account")
    public RedirectView createAccount(@ModelAttribute("employeeId") String employeeId, @ModelAttribute("teamLeaderId") String teamLeaderId, RedirectAttributes ra) {
        RedirectView rv = new RedirectView("/tom/");
        try {
            accountService.generateAccount(employeeId, teamLeaderId);
            ra.addFlashAttribute("upperNotification", "The employee record was added and the account was generated!");
        } catch (SystemException e) {
            accountService.informItAboutSystemError(e.getMessage());
            ra.addFlashAttribute("upperNotification", "There was an error in the system!");
        }
        return rv;
    }
}
