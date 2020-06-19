package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ro.go.redhomeserver.tom.dtos.TOMUserDetails;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.repositories.AccountRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class TOMUserDetailServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TOMUserDetailService tomUserDetailService;

    //loadUserByUsername
    @Test
    void should_ThrowUsernameNotFoundException_NullUsername() {
        Throwable throwable = catchThrowable(() -> tomUserDetailService.loadUserByUsername(anyString()));
        assertThat(throwable).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsername_ResultInstanceOfTOMUserDetails_UsernameFound() {
        Account account = new Account();
        Employee employee = new Employee();
        Department department= new Department("IT");
        employee.setDepartment(department);
        account.setEmployee(employee);
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        UserDetails result = tomUserDetailService.loadUserByUsername(anyString());
        assertThat(result).isInstanceOf(TOMUserDetails.class);
    }
}
