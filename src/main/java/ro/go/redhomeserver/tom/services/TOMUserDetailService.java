package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.TOMUserDetails;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.repositories.AccountRepository;

import java.util.Optional;

@Service
public class TOMUserDetailService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Autowired
    public TOMUserDetailService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(s);
        if(accountOptional.isPresent())
            return new TOMUserDetails(accountOptional.get());
        else
            throw new UsernameNotFoundException("User not found!");
    }
}
