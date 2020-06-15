package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.ResetPasswordEmail;
import ro.go.redhomeserver.tom.exceptions.*;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.ResetPasswordRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.ResetPasswordRequestRepository;


import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {

    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordService(AccountRepository accountRepository, EmailService emailService, ResetPasswordRequestRepository resetPasswordRequestRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.resetPasswordRequestRepository = resetPasswordRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private Account searchForUser(String username) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (!accountOptional.isPresent())
            throw new UserNotFoundException("User " + username + " was not found!");
        return accountOptional.get();
    }

    public String getSaltOfUser(String username) {
        try {
            return this.searchForUser(username).getSalt();
        } catch (UserNotFoundException e) {
            return "";
        }
    }

    public void addResetRequest(String username, String hostLink) throws SystemException, UserNotFoundException {
        try {
            Account acc = searchForUser(username);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY, 1);

            ResetPasswordRequest req = new ResetPasswordRequest(acc, UUID.randomUUID().toString(), calendar.getTime());
            ResetPasswordEmail data = new ResetPasswordEmail(acc, hostLink + "/validate-password-reset-request?token=" + req.getToken());
            emailService.sendEmail(data);
            resetPasswordRequestRepository.save(req);
        } catch (MailException e) {
            throw new SystemException("There was an error in sending the email!");
        }
    }

    public String identifyAccountUsingToken(String token) throws InvalidTokenException {
        Date now = new Date();
        Optional<ResetPasswordRequest> requestOptional = resetPasswordRequestRepository.findByToken(token);
        if(!requestOptional.isPresent() || requestOptional.get().getExpirationDate().compareTo(now) < 0)
            throw new InvalidTokenException("The token expired or is invalid!");

        return requestOptional.get().getAccount().getId();
    }

    public void validatePassword(String password, String verification) throws SignUpException {
        if(!password.equals(verification))
            throw new PasswordVerificationException("The password does not match!");

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
            throw new WeakPasswordException("The password is too weak!");
    }

    public void updateAccountPasswordById(String id, String password) throws SignUpException {
        try {
            Optional<Account> accountOptional = accountRepository.findById(id);
            if(!accountOptional.isPresent()) {
                throw new UserNotFoundException("User was not found!");
            }
            Account account = accountOptional.get();
            String saltedPass = password+account.getSalt();
            account.setPassword(passwordEncoder.encode(saltedPass));
            accountRepository.save(account);
            resetPasswordRequestRepository.deleteAllByAccount(account);
        } catch (UserNotFoundException e) {
            throw new SignUpException("There was an error in the sign up process!");
        }
    }

    public void activateMyAccount(String userId) {
        Optional<Account> accountOptional = accountRepository.findById(userId);
        if(accountOptional.isPresent()) {
            Account account =  accountOptional.get();
            account.setActivated(true);
            accountRepository.save(account);
        }
    }

    public boolean isUserActivated(String userId) {
        Optional<Account> accountOptional = accountRepository.findById(userId);
        return accountOptional.map(Account::isActivated).orElse(false);
    }
}
