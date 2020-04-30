package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.ResetPasswordEmail;
import ro.go.redhomeserver.tom.exceptions.*;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.ResetPassReq;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.ResetPassReqRepository;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResetPassReqRepository resetPassReqRepository;

    public Account findAccountByUsername(String username) throws LogInException {
        Account acc = accountRepository.findByUsername(username);
        if (acc == null)
            throw new UserNotFoundException();

        return acc;
    }


    public void validateData(String username, String password) throws LogInException {
        if (username.equals("") || password.equals(""))
            throw new EmptyFiledException();

    }

    public void checkCredentials(Account acc, String password) throws SystemException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String aux = password + acc.getSalt();
            if (!acc.getPassword().equals(DatatypeConverter.printHexBinary(md.digest(aux.getBytes(StandardCharsets.UTF_8)))))
                throw new PasswordMatchException();
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException();
        }
    }


    public void makeResetRequest(Account acc, String hostLink) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        ResetPassReq req = new ResetPassReq(acc, UUID.randomUUID().toString(), calendar.getTime());
        resetPassReqRepository.save(req);

        ResetPasswordEmail data = new ResetPasswordEmail(acc, hostLink + "/reset?token=" + req.getToken());
        emailService.prepareAndSend(data);
    }
}
