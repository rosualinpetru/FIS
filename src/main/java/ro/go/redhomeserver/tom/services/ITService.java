package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.CredentialsEmail;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;

import javax.transaction.SystemException;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Service
public class ITService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmployeeRepository employeeRepository;

    public void generateAccount(int id_empl, int id_tl) throws SystemException {
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

        Employee emp = employeeRepository.findById(id_empl);
        Account tl_acc = accountRepository.findByEmployee_Id(id_tl);
        try {
            int index = (emp.getName()).indexOf(" ");
            username = ((emp.getName()).substring(index + 1, index + 2) + (emp.getName()).substring(0, index)).toLowerCase();
            Account acc = new Account(username, hashedPassword, salt, emp, tl_acc);
            CredentialsEmail data = new CredentialsEmail(acc, "Account data", username, passwordToSend);
            emailService.prepareAndSend(data);
            accountRepository.save(acc);
        } catch (MailException | NullPointerException e) {
            throw new SystemException();
        }
    }

}