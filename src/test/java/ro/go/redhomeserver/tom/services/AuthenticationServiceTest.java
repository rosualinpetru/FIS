package ro.go.redhomeserver.tom.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.repositories.AccountRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    //getSaltOfUser
    @Test
    void getSaltOfUser_EmptyString_UserNotFound() {
        assertThat(authenticationService.getSaltOfUser(anyString()).equals("")).isTrue();
    }

    @Test
    void getSaltOfUser_SpecificString_UserFound() {
        Account account = new Account();
        account.setSalt("salt");
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        assertThat(authenticationService.getSaltOfUser(anyString()).equals("salt")).isTrue();
    }

    //getMyEmployeeData
    @Test
    void should_ThrowUserNotFoundException_UserNotFound() {
        Throwable throwable = catchThrowable(()->authenticationService.getMyEmployeeData(anyString()));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void should_ReturnEmployee_UserFound() {
        Account account = new Account();
        Employee employee = new Employee();
        account.setEmployee(employee);
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        try {
            assertThat(account.getEmployee()==authenticationService.getMyEmployeeData(anyString()));
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    //amITeamLeader
    @Test
    void amITeamLeader_False_UserNotFound() {
        assertThat(authenticationService.amITeamLeader(anyString())).isFalse();
    }

    @Test
    void amITeamLeader_False_UserFoundButNoMembersInTeam() {
        Account account = new Account();
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        assertThat(authenticationService.amITeamLeader(anyString())).isFalse();
    }

    @Test
    void amITeamLeader_True_UserFoundButMembersInTeam() {
        Account account = new Account();
        Set<Account> set = new LinkedHashSet<>();
        set.add(new Account());
        account.setMembers(set);
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        assertThat(authenticationService.amITeamLeader(anyString())).isTrue();
    }
}
