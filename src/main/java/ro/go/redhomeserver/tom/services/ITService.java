package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.CredentialsEmail;
import ro.go.redhomeserver.tom.dtos.PendingIssue;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.DepartmentRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import javax.transaction.SystemException;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ITService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final EmployeeRepository employeeRepository;
    private final IssueRequestRepository issueRequestRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public ITService(AccountRepository accountRepository, EmailService emailService, EmployeeRepository employeeRepository, IssueRequestRepository issueRequestRepository, DepartmentRepository departmentRepository) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.employeeRepository = employeeRepository;
        this.issueRequestRepository = issueRequestRepository;
        this.departmentRepository = departmentRepository;
    }

    public void generateAccount(int employeeId, int teamLeaderId) throws SystemException {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Optional<Account> teamLeaderOptional = accountRepository.findByEmployee_Id(teamLeaderId);
        if (!employeeOptional.isPresent() || !teamLeaderOptional.isPresent())
            throw new SystemException();

        String username;
        String password;
        String salt;

        Random random = new Random();
        int length = 16;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        password = new String(text);
        salt = password.substring(length - 6, length);
        String passwordToSend = password.substring(0, length - 6);
        String hashedPassword;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hashedPassword = DatatypeConverter.printHexBinary(md.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException();
        }

        int index = (employeeOptional.get().getName()).indexOf(" ");
        String name = employeeOptional.get().getName();
        username = (name.substring(index + 1, index + 2) + name.substring(0, index)).toLowerCase();
        if (accountRepository.findByUsername(username).isPresent()) {
            int i = 1;
            String aux = username;
            do {
                username = aux + i;
                i++;
            } while (accountRepository.findByUsername(username).isPresent());

        }

        Account acc = new Account(username, hashedPassword, salt, employeeOptional.get(), teamLeaderOptional.get());
        CredentialsEmail data = new CredentialsEmail(acc, "Account data", username, passwordToSend);

        try {
            emailService.sendEmail(data);
            accountRepository.save(acc);
        } catch (MailException | NullPointerException e) {
            throw new SystemException();
        }
    }

    public void addDepartment(String name) {
        departmentRepository.save(new Department(name));
    }

    public Iterable<Department> loadDepartments() {
        return departmentRepository.findAll();
    }

    public void informItAboutSystemError(int employeeId) {
        issueRequestRepository.save(new IssueRequest("The user with id: " + employeeId + "doesn't have an account due to some system problems!", null));
    }

    public void removeDepartment(int departmentId) {
        StringBuilder str = new StringBuilder("The following employees don't have a department: \n");
        List<Employee> lst = employeeRepository.findAllByDepartment_Id(departmentId);
        for (Employee e : lst) {
            str.append(e.getName());
            str.append("\n");
            e.setDepartment(null);
            employeeRepository.save(e);
        }
        issueRequestRepository.save(new IssueRequest(str.toString(), null));
        departmentRepository.deleteById(departmentId);
    }

    public void addIssueRequest(Map<String, String> params) {
        Optional<Account> accountOptional = accountRepository.findById(Integer.parseInt(params.get("myId")));
        accountOptional.ifPresent(account -> issueRequestRepository.save(new IssueRequest(params.get("description"), account)));
    }

    public List<PendingIssue> loadAllPendingIssueRequests() {
        Comparator<IssueRequest> compareByIssueReq = Comparator.comparingInt(i -> i.getAccount().getEmployee().getDepartment().getId());
        List<IssueRequest> lst = (List<IssueRequest>) issueRequestRepository.findAll();
        lst.sort(compareByIssueReq);
        return lst.stream().map(s -> new PendingIssue(s.getId(), s.getAccount().getEmployee().getDepartment().getName(), s.getAccount().getEmployee().getName(), s.getDescription())).collect(Collectors.toList());
    }

    public void deleteIssueRequestById(int issueRequestId) {
        issueRequestRepository.deleteById(issueRequestId);
    }

    public void removeEmployee(int employeeId) {
        Optional<Employee> accountOptional = employeeRepository.findById(employeeId);
        if (accountOptional.isPresent()) {
            employeeRepository.deleteById(employeeId);
        }
    }

    public void updateTeamLeader(int employeeId1, int employeeId2) {
        Optional<Employee> employeeOptional1 = employeeRepository.findById(employeeId1);
        Optional<Employee> employeeOptional2 = employeeRepository.findById(employeeId2);
        if (employeeOptional1.isPresent() && employeeOptional2.isPresent()) {
            Account account = employeeOptional1.get().getAccount();
            account.setTeamLeader(employeeOptional2.get().getAccount());
            accountRepository.save(account);
        }
    }
}