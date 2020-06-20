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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TOMUserDetailServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TOMUserDetailService tomUserDetailService;

    @Test
    void loadUserByUsernameShouldThrowUsernameNotFoundExceptionIfUsernameNotFound() {
        Throwable throwable = catchThrowable(() -> tomUserDetailService.loadUserByUsername(anyString()));
        assertThat(throwable).isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void loadUserByUsernameShoundBeResultInstanceOfTOMUserDetailsIfUsernameFound() {
        Account account = new Account();
        Employee employee = new Employee();
        Department department = new Department("IT");
        employee.setDepartment(department);
        account.setEmployee(employee);
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        UserDetails result = tomUserDetailService.loadUserByUsername(anyString());
        assertThat(result).isInstanceOf(TOMUserDetails.class);
    }
}
