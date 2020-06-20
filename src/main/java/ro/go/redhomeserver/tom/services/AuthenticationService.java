package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.repositories.AccountRepository;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final AccountRepository accountRepository;

    @Autowired
    public AuthenticationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String getSaltOfUser(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent())
            return accountOptional.get().getSalt();
        else
            return "";
    }

    public boolean amITeamLeader(String username) {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        return accountOptional.filter(account -> !account.getMembers().isEmpty()).isPresent();
    }

    public Employee getMyEmployeeData(String username) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if (accountOptional.isPresent())
            return accountOptional.get().getEmployee();
        else
            throw new UserNotFoundException("The user was not found!");
    }
}
