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

@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;

    public Account findAccountByUsername(String username) throws UserNotFoundException {
        Account acc = accountRepository.findByUsername(username);
        if (acc == null)
            throw new UserNotFoundException();

        return acc;
    }

    public void checkCredentials(Account acc, String password) throws SystemException, PasswordMatchException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String aux = password + acc.getSalt();
            if (!acc.getPassword().equals(DatatypeConverter.printHexBinary(md.digest(aux.getBytes(StandardCharsets.UTF_8)))))
                throw new PasswordMatchException();
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException();
        }
    }
}
