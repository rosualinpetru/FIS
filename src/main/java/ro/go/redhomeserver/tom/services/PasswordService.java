package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
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
public class PasswordService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResetPassReqRepository resetPassReqRepository;


    public void makeResetRequest(Account acc, String hostLink) throws SystemException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        ResetPassReq req = new ResetPassReq(acc, UUID.randomUUID().toString(), calendar.getTime());
        try {
            ResetPasswordEmail data = new ResetPasswordEmail(acc, hostLink + "/validateReset?token=" + req.getToken());
            emailService.prepareAndSend(data);
            resetPassReqRepository.save(req);
        } catch (MailException e) {
            throw new SystemException();
        }

    }

    public int identifyAccount(String token) throws InvalidTokenException {
        Date now = new Date();
        ResetPassReq req = resetPassReqRepository.findByToken(token);
        if(req == null || req.getExpirationDate().compareTo(now) < 0)
            throw new InvalidTokenException();

        return req.getAccount().getId();
    }

    public void validatePassword(String password, String verification) throws SignUpException {
        if(!password.equals(verification))
            throw new PasswordMismatchException();

        int iPasswordScore = 0;

        if( password.matches("(?=.*[0-9]).*") )
            iPasswordScore += 2;

        if( password.matches("(?=.*[a-z]).*") )
            iPasswordScore += 2;

        if( password.matches("(?=.*[A-Z]).*") )
            iPasswordScore += 2;

        if( password.matches("(?=.*[~!@#$%^&*()_-]).*") )
            iPasswordScore += 2;

        if( password.length() < 8 || iPasswordScore < 4)
            throw new WeakPasswordException();
    }

    public void updateAccountPasswordById(int id, String password) throws SystemException {
        try {
            Account acc = accountRepository.findById(id);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String saltedPass = password+acc.getSalt();
            acc.setPassword(DatatypeConverter.printHexBinary(md.digest(saltedPass.getBytes(StandardCharsets.UTF_8))));
            accountRepository.save(acc);
            resetPassReqRepository.deleteAllByAccount(acc);
        } catch (NoSuchAlgorithmException | NullPointerException e) {
            throw new SystemException();
        }
    }

}
