package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.dtos.RequestStatus;
import ro.go.redhomeserver.tom.dtos.RequestType;
import ro.go.redhomeserver.tom.exceptions.FileStorageException;
import ro.go.redhomeserver.tom.exceptions.NotEnoughDaysException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Optional<Employee> accountOptional = employeeRepository.findById(employeeId);
        if (accountOptional.isPresent()) {
            employeeRepository.deleteById(employeeId);
        }
    }

    public void updateTeamLeader(String employeeId1, String employeeId2) {
        Optional<Employee> employeeOptional1 = employeeRepository.findById(employeeId1);
        Optional<Employee> employeeOptional2 = employeeRepository.findById(employeeId2);
        if (employeeOptional1.isPresent() && employeeOptional2.isPresent()) {
            Account account = employeeOptional1.get().getAccount();
            account.setTeamLeader(employeeOptional2.get().getAccount());
            accountRepository.save(account);
        }
    }
}
