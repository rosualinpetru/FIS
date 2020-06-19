package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.DepartmentRepository;
import ro.go.redhomeserver.tom.repositories.EmployeeRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ITServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private IssueRequestRepository issueRequestRepository;

    @InjectMocks
    private ITService itService;

    //addDepartment
    @Test
    void checkIfDepartmentIsAdded() {
        when(departmentRepository.save(any(Department.class))).then(invocation -> invocation.getArguments()[0]);
        Department result = itService.addDepartment("IT");
        assertThat(result.getName().equals("IT"));
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
        doAnswer(invocation -> departments.removeIf(i->i.getId().equals(invocation.getArguments()[0]))).when(departmentRepository).deleteById(anyString());

        itService.removeDepartment("id1");
        verify(issueRequestRepository, times(1)).save(any(IssueRequest.class));
        assertThat(departments.contains(it)).isFalse();
        assertThat(departments.contains(hr));
        assertThat(employeesOfDepartment.size()==2);
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
        doAnswer(invocation -> departments.removeIf(i->i.getId().equals(invocation.getArguments()[0]))).when(departmentRepository).deleteById(anyString());

        itService.removeDepartment("id1");
        verify(issueRequestRepository, times(0)).save(any(IssueRequest.class));
        assertThat(departments.contains(it)).isFalse();
        assertThat(departments.contains(hr));
    }
}
