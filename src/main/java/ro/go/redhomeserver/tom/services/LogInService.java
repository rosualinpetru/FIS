package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.exceptions.PasswordMatchException;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.repositories.AccountRepository;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class LogInService {

    AccountRepository accountRepository;

    @Autowired
    public LogInService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account searchForUser(String username) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (!accountOptional.isPresent())
            throw new UserNotFoundException();
        return accountOptional.get();
    }

    public void checkCredentials(Account account, String password) throws SystemException, PasswordMatchException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String aux = password + account.getSalt();
            if (!account.getPassword().equals(DatatypeConverter.printHexBinary(md.digest(aux.getBytes(StandardCharsets.UTF_8)))))
                throw new PasswordMatchException();
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException();
        }
    }
}
