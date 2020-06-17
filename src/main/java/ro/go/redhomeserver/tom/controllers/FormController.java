package ro.go.redhomeserver.tom.controllers;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.go.redhomeserver.tom.services.FormService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class FormController {

    private final FormService formService;

    @Autowired
    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping({"/update-delete-employee-form", "/update-change-team-leader-form-without-me"})
    @ResponseBody
    public List<Pair<String, String>> getEmployeesOfDepartment(@RequestParam("departmentId") String departmentId, Authentication authentication) {
        return formService.loadEmployeesOfDepartmentByIdExceptMe(departmentId, authentication.getName()).stream().map(s -> new Pair<>(s.getId(), s.getEmployee().getName())).collect(Collectors.toList());
    }

    @GetMapping({"/update-sign-up-form", "/update-change-team-leader-form", "update-company-schedule-form"})
    @ResponseBody
    public List<Pair<String, String>> getEmployeesOfDepartment(@RequestParam("departmentId") String departmentId) {
        return formService.loadEmployeesOfDepartmentById(departmentId).stream().map(s -> new Pair<>(s.getId(), s.getEmployee().getName())).collect(Collectors.toList());
    }
}
