package ro.go.redhomeserver.tom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class HRController {

    @Autowired
    private EmployeeRepository employeeRepository;


    @PostMapping("/resolveSignUP")
    @RequestMapping(method = RequestMethod.GET)
    public String resolveSignUP(@RequestParam ("name") String name, @RequestParam ("address") String address  , @RequestParam ("tel") String tel , @RequestParam ("salary") String salary, @RequestParam ("email") String email, @RequestParam ("empl_date")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date empl_date, String department, RedirectAttributes RA)
        {
            Employee toAdd = new Employee(name, address, tel, 10, email, empl_date, new Department("HR"));
            employeeRepository.save(toAdd);
            try {
                //Employee result = employeeRepository.findByEmail(email);
                RA.addFlashAttribute("employeeData", toAdd);
                return "redirect:/createAccount";
            } catch(Exception e){
        RA.addFlashAttribute("error", "Email already in use! ");
            }

        return "redirect:/signUp";

    }

}
