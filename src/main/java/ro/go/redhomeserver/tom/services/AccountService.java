package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.exceptions.LogInException;
import ro.go.redhomeserver.tom.exceptions.PasswordMatchException;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.repositories.AccountRepository;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

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
 }
