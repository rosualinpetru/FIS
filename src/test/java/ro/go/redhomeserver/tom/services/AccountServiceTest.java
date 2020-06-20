package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailPreparationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.go.redhomeserver.tom.emails.EmailData;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private IssueRequestRepository issueRequestRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    //informItAboutSystemError
    @Test
    void InformItAboutSystemError_Null_issueRequesterAccount() {
        ArrayList<IssueRequest> issueRequests = new ArrayList<>();
        IssueRequest ir = new IssueRequest("", null);
        doAnswer(invocation -> {issueRequests.add(ir); return ir;}).when(issueRequestRepository).save(any(IssueRequest.class));
        accountService.informItAboutSystemError(anyString());

        verify(issueRequestRepository, times(1)).save(any(IssueRequest.class));
        assertThat(issueRequests.contains(ir)).isTrue();
        assertThat(issueRequests.get(0).getAccount()).isNull();
    }

    //generateAccount
    @Test
    void should_ThrowSystemException_EmployeeNotFound() {
        Throwable exception = catchThrowable(() -> accountService.generateAccount("", ""));
        assertThat(exception).isInstanceOf(SystemException.class);
    }

    @Test
    void should_ThrowSystemException_MailExceptionByEmailService() {
        Employee emp = new Employee("Rosu Alin", null, null, 0, "rosualinpetru@gmail.com", new Date(), null);
        when(employeeRepository.findById("id")).thenReturn(java.util.Optional.of(emp));
        doThrow(new MailPreparationException("Failed")).when(emailService).sendEmail(any(EmailData.class));
        Throwable exception = catchThrowable(() -> accountService.generateAccount("id", ""));
        assertThat(exception).isInstanceOf(SystemException.class);
    }

    @Test
    void should_SendEmail_IfAccountGenerated() {
        Employee emp = new Employee("Rosu Alin", null, null, 0, "rosualinpetru@gmail.com", new Date(), null);
        when(employeeRepository.findById("id")).thenReturn(java.util.Optional.of(emp));
        try {
           accountService.generateAccount("id", anyString());
        } catch (Exception e) {
            fail("Exception interfered!");
        }
        verify(emailService, times(1)).sendEmail(any(EmailData.class));
    }

    @Test
    void theGeneratedUserHasACorrectPassword(){
        Employee emp = new Employee("Rosu Alin", null, null, 0, null, new Date(), null);
        when(employeeRepository.findById("id")).thenReturn(java.util.Optional.of(emp));
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Account result = null;
        try {
            result = accountService.generateAccount("id", anyString());
        } catch (Exception e) {
            fail("Exception interfered!");
        }

        assertThat(result).isNotNull();
        verify(passwordEncoder, times(1)).encode(anyString());
        assertThat(result.getPassword().length()>=45 && result.getPassword().length()<=75).isNotNull();
        assertThat(result.getSalt().length()==6).isTrue();
    }

    @Test
    void theGeneratedUserHasACorrectDataWithoutTeamLeader(){
        Employee emp = new Employee("Rosu Alin", null, null, 0, null, new Date(), null);
        when(employeeRepository.findById("id")).thenReturn(java.util.Optional.of(emp));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Account result = null;
        try {
            result = accountService.generateAccount("id", anyString());
        } catch (Exception e) {
            fail("Exception interfered!");
        }

        assertThat(result).isNotNull();
        assertThat(result.getRemainingDays()==30).isTrue();
        assertThat(result.getTeamLeader()).isNull();
    }

    @Test
    void theGeneratedUserHasACorrectDataWithTeamLeader(){
        Account tl = new Account();
        Employee emp = new Employee("Rosu Alin", null, null, 0, null, new Date(), null);
        when(employeeRepository.findById("id")).thenReturn(java.util.Optional.of(emp));
        when(accountRepository.findByEmployee_Id(anyString())).thenReturn(java.util.Optional.of(tl));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Account result = null;
        try {
            result = accountService.generateAccount("id", anyString());
        } catch (Exception e) {
            fail("Exception interfered!");
        }

        assertThat(result).isNotNull();
        assertThat(result.getRemainingDays()==21).isTrue();
        assertThat(result.getTeamLeader()).isNotNull();
    }

    @Test
    void employeesWithSameInitialsGetDifferentAccountUsernames() {
        Employee emp = new Employee("Rosu Alin", null, null, 0, null, new Date(), null);
        when(employeeRepository.findById("id")).thenReturn(java.util.Optional.of(emp));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(accountRepository.findByUsername("arosu")).thenReturn(java.util.Optional.of(new Account()));
        Account result = null;
        try {
            result = accountService.generateAccount("id", anyString());
        } catch (Exception e) {
            fail("Exception interfered!");
        }
        assertThat(result).isNotNull();
        assertThat(result.getUsername().equals("arosu1")).isTrue();
    }
}
