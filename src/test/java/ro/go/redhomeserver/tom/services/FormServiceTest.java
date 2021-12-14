package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.DepartmentRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private FormService formService;

    @Test
    void loadAccountMethodsMustReturnResultOfAccountRepositoryQuery() {
        ArrayList<Account> accountList = new ArrayList<>();
        when(accountRepository.findAllByEmployee_Department_Id(anyString())).thenReturn(accountList);
        when(accountRepository.findAllByEmployee_Department_IdAndUsernameNot(anyString(), anyString())).thenReturn(accountList);
        assertThat(formService.loadAccountsOfDepartmentById(anyString()) == accountList).isTrue();
        assertThat(formService.loadAccountsOfDepartmentByIdExceptMe(anyString(), anyString()) == accountList).isTrue();
    }

    @Test
    void loadDepartmentMustReturnResultOfDepartmentRepositoryQuery() {
        ArrayList<Department> departments = new ArrayList<>();
        when(departmentRepository.findAll()).thenReturn(departments);
        assertThat(formService.loadDepartments() == departments).isTrue();
    }

    @Test
    void loadPossibleDelegatesShouldThrowUserNotFoundExceptionIfUsernameNotFound() {
        Throwable throwable = catchThrowable(() -> formService.loadPossibleDelegates(null));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void loadPossibleDelegatesShouldBeNullIfNotATeamLeader() {
        Account account = new Account();
        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(account));
        try {
            assertThat(formService.loadPossibleDelegates(anyString())).isNull();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    @Test
    void loadPossibleDelegatesShouldReturnColleaguesIfUserIsTeamLeaderAndUserHasATeamLeader() {
        Account me = new Account();
        me.setId(UUID.randomUUID().toString());
        Account account;
        Account teamLeader = new Account();
        teamLeader.setId(UUID.randomUUID().toString());
        Set<Account> myMembers = new HashSet<>();
        Set<Account> members = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            account = new Account();
            account.setId(UUID.randomUUID().toString());
            account.setTeamLeader(teamLeader);
            members.add(account);
            account = new Account();
            account.setId(UUID.randomUUID().toString());
            account.setTeamLeader(me);
            myMembers.add(account);
        }
        me.setTeamLeader(teamLeader);
        members.add(me);
        teamLeader.setMembers(members);
        me.setMembers(myMembers);

        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(me));
        try {
            List<Account> result = formService.loadPossibleDelegates(anyString());
            assertThat(result.size() == 3).isTrue();
            assertThat(result.contains(me)).isFalse();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    @Test
    void loadPossibleDelegatesShouldReturnColleaguesWithNullTeamLeaderOfSameDepartmentIfPresentIfUserIsTeamLeaderAndUserHasNotATeamLeader() {
        Account me = new Account();
        me.setId(UUID.randomUUID().toString());
        Employee employee = new Employee();
        employee.setDepartment(new Department());
        me.setEmployee(employee);
        Account account;
        ArrayList<Account> accountsWithNullTlOfSameDep = new ArrayList<>();
        accountsWithNullTlOfSameDep.add(me);
        Set<Account> myMembers = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            account = new Account();
            account.setId(UUID.randomUUID().toString());
            accountsWithNullTlOfSameDep.add(account);
            account = new Account();
            account.setId(UUID.randomUUID().toString());
            account.setTeamLeader(me);
            myMembers.add(account);
        }
        me.setMembers(myMembers);

        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(me));
        when(accountRepository.findAllByTeamLeaderIsNullAndEmployee_Department(any(Department.class))).thenReturn(accountsWithNullTlOfSameDep);
        try {
            List<Account> result = formService.loadPossibleDelegates(anyString());
            assertThat(result.size()).isEqualTo(2);
            assertThat(result.contains(me)).isFalse();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    @Test
    void loadPossibleDelegatesShouldReturnTeamMembersIfColleaguesAreMissingIfUserIsTeamLeaderAndUserHasNotATeamLeader() {
        Account me = new Account();
        Employee employee = new Employee();
        employee.setDepartment(new Department());
        me.setEmployee(employee);
        Account account;
        ArrayList<Account> accountsWithNullTlOfSameDep = new ArrayList<>();
        accountsWithNullTlOfSameDep.add(me);
        Set<Account> myMembers = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            account = new Account();
            account.setId("" + i);
            account.setTeamLeader(me);
            myMembers.add(account);
        }
        me.setMembers(myMembers);

        when(accountRepository.findByUsername(anyString())).thenReturn(Optional.of(me));
        when(accountRepository.findAllByTeamLeaderIsNullAndEmployee_Department(any(Department.class))).thenReturn(accountsWithNullTlOfSameDep);
        try {
            List<Account> result = formService.loadPossibleDelegates(anyString());
            assertThat(result.size() == 2).isTrue();
            assertThat(result.contains(me)).isFalse();
            assertThat(result.equals(new ArrayList<>(me.getMembers()))).isTrue();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }
}
