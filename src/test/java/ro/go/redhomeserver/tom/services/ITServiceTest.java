package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.DepartmentRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ITServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private IssueRequestRepository issueRequestRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private ITService itService;

    //addDepartment
    @Test
    void checkIfDepartmentIsAdded() {
        when(departmentRepository.save(any(Department.class))).then(invocation -> invocation.getArguments()[0]);
        Department result = itService.addDepartment("IT");
        assertThat(result.getName().equals("IT")).isTrue();
    }

    //removeDepartment
    @Test
    void deletionNeedsToAddAnIssueIfThereAreEmployeesInTheDepartment() {
        ArrayList<Department> departments= new ArrayList<>();
        Department it = new Department();
        Department hr = new Department();
        it.setName("IT");
        it.setId("id1");
        hr.setName("HR");
        hr.setId("id2");
        departments.add(it);
        departments.add(hr);

        ArrayList<Employee> employeesOfDepartment = new ArrayList<>();
        Employee e1,e2,e3;
        e1 = new Employee();
        e3 = new Employee();
        e2 = new Employee();
        e1.setId("1");
        e1.setId("2");
        e1.setId("3");
        e1.setDepartment(it);
        e2.setDepartment(it);
        e3.setDepartment(hr);

        employeesOfDepartment.add(e1);
        employeesOfDepartment.add(e2);

        when(employeeRepository.findAllByDepartment_Id(anyString())).thenReturn(employeesOfDepartment);
        doAnswer(invocation -> departments.removeIf(i -> i.getId().equals(invocation.getArguments()[0]))).when(departmentRepository).deleteById(anyString());

        itService.removeDepartment("id1");
        verify(issueRequestRepository, times(1)).save(any(IssueRequest.class));
        assertThat(departments.contains(it)).isFalse();
        assertThat(departments.contains(hr)).isTrue();
        assertThat(employeesOfDepartment.size() == 2).isTrue();
        assertThat(employeesOfDepartment.get(0).getDepartment()).isNull();
        assertThat(employeesOfDepartment.get(1).getDepartment()).isNull();
        assertThat(e3.getDepartment()).isNotNull();
    }

    @Test
    void ifNoEmployeesInDepartmentJustDeleteIt() {
        ArrayList<Department> departments= new ArrayList<>();
        Department it = new Department();
        Department hr = new Department();
        it.setName("IT");
        it.setId("id1");
        hr.setName("HR");
        hr.setId("id2");
        departments.add(it);
        departments.add(hr);

        ArrayList<Employee> employeesOfDepartment = new ArrayList<>();

        when(employeeRepository.findAllByDepartment_Id(anyString())).thenReturn(employeesOfDepartment);
        doAnswer(invocation -> departments.removeIf(i -> i.getId().equals(invocation.getArguments()[0]))).when(departmentRepository).deleteById(anyString());

        itService.removeDepartment("id1");
        verify(issueRequestRepository, times(0)).save(any(IssueRequest.class));
        assertThat(departments.contains(it)).isFalse();
        assertThat(departments.contains(hr)).isTrue();
    }

    //removeEmployee
    @Test
    void deleteEmployeeByIdShouldNeverBeCalledIfEmployeeNotFound() {
        itService.removeEmployee(null);
        verify(employeeRepository, times(0)).deleteById(anyString());
    }

    @Test
    void employeeShouldBeRemovedIfFound() {
        Employee e;
        ArrayList<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            e = new Employee();
            e.setId("id" + i);
            employees.add(e);
        }

        when(employeeRepository.findById(anyString())).then(invocation -> java.util.Optional.of(employees.get(employees.size() - 1)));
        doAnswer(invocation -> employees.removeIf(i -> i.getId().equals(invocation.getArguments()[0]))).when(employeeRepository).deleteById(anyString());
        for (int i = 0; i < 10; i++) {
            itService.removeEmployee("id" + i);
            verify(employeeRepository, times(i + 1)).deleteById(anyString());
            assertThat(employees.size() == 9 - i).isTrue();
            e = new Employee();
            e.setId("id" + i);
            assertThat(employees.contains(e)).isFalse();
        }
    }

    //updateTeamLeader
    @Test
    void should_DoNothing_IfFirstEmployeeWasNotFound() {
        itService.updateTeamLeader(null, null);
        verify(accountRepository, times(0)).save(any(Account.class));
    }

    @Test
    void theEmployeeShouldHaveHisTeamLeaderNullIfSecondIsNotFound() {
        Employee employee = new Employee();
        employee.setId("id1");
        Account account = new Account();
        Account teamLeader = new Account();
        account.setTeamLeader(teamLeader);
        employee.setAccount(account);
        when(employeeRepository.findById("id1")).thenReturn(Optional.of(employee));
        doAnswer(invocation -> {
            ((Account) (invocation.getArguments()[0])).setTeamLeader(null);
            return invocation.getArguments()[0];
        }).when(accountRepository).save(any(Account.class));
        assertThat(employee.getAccount().getTeamLeader() == teamLeader).isTrue();
        itService.updateTeamLeader("id1", "");
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(employee.getAccount().getTeamLeader()).isNull();
    }

    @Test
    void employee1ShouldHaveTheTeamLeaderEmployee2IfBothFound() {
        Employee employee1 = new Employee();
        employee1.setId("id1");
        Employee employee2 = new Employee();
        employee1.setId("id2");

        Account account1 = new Account();
        Account teamLeader = new Account();
        account1.setTeamLeader(teamLeader);
        employee1.setAccount(account1);

        Account account2 = new Account();
        employee2.setAccount(account2);

        when(employeeRepository.findById("id1")).thenReturn(Optional.of(employee1));
        when(employeeRepository.findById("id2")).thenReturn(Optional.of(employee2));
        doAnswer(invocation -> {
            ((Account) (invocation.getArguments()[0])).setTeamLeader(employee2.getAccount());
            return invocation.getArguments()[0];
        }).when(accountRepository).save(any(Account.class));

        assertThat(employee1.getAccount().getTeamLeader() == teamLeader).isTrue();
        itService.updateTeamLeader("id1", "id2");
        verify(accountRepository, times(1)).save(any(Account.class));
        assertThat(employee1.getAccount().getTeamLeader() == teamLeader).isFalse();
        assertThat(employee1.getAccount().getTeamLeader() == employee2.getAccount()).isTrue();
    }
}
