package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.services.ITService;
import javax.transaction.SystemException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


@Controller
public class ITController {


    @Autowired
    private ITService itService;

    @GetMapping("/createAccount")
    public String createAccount(@ModelAttribute("emplId") int id_empl, @ModelAttribute("tlId") int id_tl) {
        try {
            itService.generateAccount(id_empl, id_tl);
        }catch (SystemException e){
            System.out.println(e);//TODO:Resolve exception
        }
        return "redirect:/";

    }
}