package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;

import java.util.Optional;

@Service
public class EmployeeService {

    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(AccountRepository accountRepository, EmployeeRepository employeeRepository) {
        this.accountRepository = accountRepository;
        this.employeeRepository = employeeRepository;
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
