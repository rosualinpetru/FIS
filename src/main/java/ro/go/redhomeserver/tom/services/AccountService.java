package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.EmailData;
import ro.go.redhomeserver.tom.dtos.ResetPasswordEmail;
import ro.go.redhomeserver.tom.exceptions.LogInException;
import ro.go.redhomeserver.tom.exceptions.PasswordMatchException;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.ResetPassReq;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.ResetPassReqRepository;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResetPassReqRepository resetPassReqRepository;


    public Account checkCredentials(String username, String password) throws LogInException, SystemException {
        Account acc = accountRepository.findByUsername(username);
        if (acc != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                String aux = password + acc.getSalt();
                if (acc.getPassword().equals(DatatypeConverter.printHexBinary(md.digest(aux.getBytes(StandardCharsets.UTF_8))))) {
                    return acc;
                } else {
                    throw new PasswordMatchException();
                }
            } catch (NoSuchAlgorithmException e) {
                throw new SystemException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }


    public void sendResetEmail(String username, HttpServletRequest request) throws UserNotFoundException{
        Account acc = accountRepository.findByUsername(username);
        if(acc == null) {
            throw new UserNotFoundException();
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY, 1);

            ResetPassReq req = new ResetPassReq();
            req.setAccount_req(acc);
            req.setExpirationDate(calendar.getTime());
            req.setToken(UUID.randomUUID().toString());
            resetPassReqRepository.save(req);

            String appUrl = request.getScheme() + "://" + request.getServerName();

            ResetPasswordEmail data = new ResetPasswordEmail( appUrl + ":8080/reset?token=" + req.getToken(), username);
            emailService.prepareAndSend(acc.getEmployee().getEmail(), data);
        }
    }
 }
