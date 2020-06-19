package ro.go.redhomeserver.tom.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.repositories.AccountRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ActivationService activationService;

    //isAccountActivated
    @Test
    void isUserActivated_False_AccountNotFound() {
        when(accountRepository.findById(anyString())).thenReturn(java.util.Optional.of(new Account()));
        assertThat(activationService.isUserActivated(anyString())).isFalse();
    }

    @Test
    void isUserActivated_False_NewAccount() {
        when(accountRepository.findById(anyString())).thenReturn(java.util.Optional.of(new Account()));
        assertThat(activationService.isUserActivated(anyString())).isFalse();
    }

    //activateMyAccount
    @Test
    void should_ActivateAccountAndCallSaveOnce_AccountFound() {
        Account account = new Account();
        when(accountRepository.findById(anyString())).thenReturn(java.util.Optional.of(account));
        assertThat(account.isActivated()).isFalse();
        activationService.activateMyAccount(anyString());
        assertThat(account.isActivated()).isTrue();
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void should_NeverCallSave_AccountNotFound() {
        activationService.activateMyAccount(anyString());
        verify(accountRepository, times(0)).save(any(Account.class));
    }
}
