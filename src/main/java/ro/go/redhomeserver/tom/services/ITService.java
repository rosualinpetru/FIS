package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.DepartmentRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import java.util.*;

@Service
public class ITService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final IssueRequestRepository issueRequestRepository;

    @Autowired
    public ITService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, IssueRequestRepository issueRequestRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.issueRequestRepository = issueRequestRepository;
    }

    public Department addDepartment(String name) {
        Department department = new Department(name);
        return departmentRepository.save(department);
    }

    public void removeDepartment(String departmentId) {
        List<Employee> lst = employeeRepository.findAllByDepartment_Id(departmentId);
        if (!lst.isEmpty()) {
            StringBuilder str = new StringBuilder("The following employees don't have a department: \n");
            for (Employee e : lst) {
                str.append(e.getName());
                str.append("\n");
                e.setDepartment(null);
                employeeRepository.save(e);
            }
            issueRequestRepository.save(new IssueRequest(str.toString(), null));
        }
        departmentRepository.deleteById(departmentId);
    }
}