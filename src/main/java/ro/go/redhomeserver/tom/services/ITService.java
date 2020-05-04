package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.CredentialsEmail;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueReq;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.IssueReqRepository;

import javax.transaction.SystemException;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

@Service
public class ITService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private IssueReqRepository issueReqRepository;

    public void reportIssueWithData(Map<String, String> params) {

        issueReqRepository.save(new IssueReq(params.get("description" ),accountRepository.findById(Integer.parseInt(params.get("myId")))));   // to save the issue req in the data base

    }

    public void generateAccount(int id_empl, int id_tl) throws SystemException {
        Employee emp = employeeRepository.findById(id_empl);
        Account tl_acc = accountRepository.findByEmployee_Id(id_tl);

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

        int index = (emp.getName()).indexOf(" ");
        String name = emp.getName();
        username = (name.substring(index + 1, index + 2) + name.substring(0, index)).toLowerCase();
        if(accountRepository.findByUsername(username)!=null) {
            int i = 1;
            String aux = username;
            do
            {
                username = aux + i;
                i++;
            } while(accountRepository.findByUsername(username)!=null);

        }
        Account acc = new Account(username, hashedPassword, salt, emp, tl_acc);
        CredentialsEmail data = new CredentialsEmail(acc, "Account data", username, passwordToSend);

        try {

            emailService.prepareAndSend(data);
            accountRepository.save(acc);
        } catch (MailException | NullPointerException e) {
            throw new SystemException();
        }
    }

    public void informItAboutError(int id_empl) {
        issueReqRepository.save(new IssueReq("The user with id: " + id_empl + "doesn't have an account due to some system problems!", null));
    }

}