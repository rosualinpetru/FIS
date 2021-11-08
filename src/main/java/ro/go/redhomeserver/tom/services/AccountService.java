package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.emails.CredentialsEmail;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final EmployeeRepository employeeRepository;
    private final IssueRequestRepository issueRequestRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, EmailService emailService, EmployeeRepository employeeRepository, IssueRequestRepository issueRequestRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.employeeRepository = employeeRepository;
        this.issueRequestRepository = issueRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account generateAccount(String employeeId, String teamLeaderId) throws SystemException {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Optional<Account> teamLeaderOptional = accountRepository.findByEmployee_Id(teamLeaderId);

        if (!employeeOptional.isPresent())
            throw new SystemException("There was an error in the system!");

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

        hashedPassword = passwordEncoder.encode(password);

        int index = employeeOptional.get().getName().indexOf(" ");
        String name = employeeOptional.get().getName();
        username = (name.charAt(index + 1) + name.substring(0, index)).toLowerCase();
        if (accountRepository.findByUsername(username).isPresent()) {
            int i = 1;
            String aux = username;
            do {
                username = aux + i;
                i++;
            } while (accountRepository.findByUsername(username).isPresent());

        }

        Account acc;
        if(teamLeaderOptional.isPresent())
            acc = new Account(username, hashedPassword, salt, employeeOptional.get(), teamLeaderOptional.get(), 21);
        else
            acc = new Account(username, hashedPassword, salt, employeeOptional.get(), null, 30);

        CredentialsEmail data = new CredentialsEmail(acc, username, passwordToSend);

        try {
            emailService.sendEmail(data);
            return accountRepository.save(acc);
        } catch (MailException e) {
            throw new SystemException("The user with id: " + employeeId + "doesn't have an account due to some system problems!");
        }
    }

    public IssueRequest informItAboutSystemError(String message) {
        return issueRequestRepository.save(new IssueRequest(message, null));
    }
}
