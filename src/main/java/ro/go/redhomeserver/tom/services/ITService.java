package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.DepartmentRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import java.util.*;

@Service
public class ITService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final IssueRequestRepository issueRequestRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ITService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, IssueRequestRepository issueRequestRepository, AccountRepository accountRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.issueRequestRepository = issueRequestRepository;
        this.accountRepository = accountRepository;
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

    public void removeEmployee(String employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            employeeRepository.deleteById(employeeId);
        }
    }

    public void updateTeamLeader(String employeeId1, String employeeId2) {
        Optional<Employee> employeeOptional1 = employeeRepository.findById(employeeId1);
        if (employeeOptional1.isPresent()) {
            if (employeeId2.equals("")) {
                Account account = employeeOptional1.get().getAccount();
                account.setTeamLeader(null);
                accountRepository.save(account);
            } else {
                Optional<Employee> employeeOptional2 = employeeRepository.findById(employeeId2);
                if (employeeOptional2.isPresent()) {
                    Account account = employeeOptional1.get().getAccount();
                    account.setTeamLeader(employeeOptional2.get().getAccount());
                    accountRepository.save(account);
                }
            }
        }
    }
}