package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.exceptions.MissingDepartmentException;
import ro.go.redhomeserver.tom.exceptions.UsedEmailException;
import ro.go.redhomeserver.tom.models.*;
import ro.go.redhomeserver.tom.repositories.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HRServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private HolidayRequestRepository holidayRequestRepository;
    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private HRService hrService;

    //checkIfEmailIsAvailable
    @Test
    void checkIfEmailIsAvailableShouldThrowUsedEmailExceptionIfEmployeeFound() {
        Map<String, String> map = new HashMap<>();
        map.put("email", "");
        when(employeeRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new Employee()));
        Throwable throwable = catchThrowable(() -> hrService.checkIfEmailIsAvailable(map));
        assertThat(throwable).isInstanceOf(UsedEmailException.class);
    }

    @Test
    void checkIfEmailIsAvailableShouldDoNothingIfEmployeeNotFound() {
        Map<String, String> map = new HashMap<>();
        map.put("email", "");
        Throwable throwable = catchThrowable(() -> hrService.checkIfEmailIsAvailable(map));
        assertThat(throwable).isNull();
    }

    //loadRequestsOfDepartment
    @Test
    void loadRequestsOfDepartmentShouldBeTheResultOfHolidayRequestRepositoryQuery() {
        List<HolidayRequest> holidayRequests = new ArrayList<>();
        when(holidayRequestRepository.findAllByRequester_Employee_Department_Id(anyString())).thenReturn(holidayRequests);
        assertThat(hrService.loadRequestsOfDepartment("") == holidayRequests).isTrue();
    }

    //addFeedback
    @Test
    void addFeedbackShouldBeNullIfAccountOfRequestAreNotFound() {
        assertThat(hrService.addFeedback(null, null, null)).isNull();
    }

    @Test
    void addFeedbackShouldSaveFeedbackIfAccountAndRequestFound() {
        Account account = new Account();
        HolidayRequest holidayRequest = new HolidayRequest();
        when(holidayRequestRepository.findById(anyString())).thenReturn(java.util.Optional.of(holidayRequest));
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        when(feedbackRepository.save(any(Feedback.class))).then(invocation -> invocation.getArguments()[0]);
        Feedback result = hrService.addFeedback("", "desc", "");
        assertThat(result.getReporter() == account).isTrue();
        assertThat(result.getDescription().equals("desc")).isTrue();
        assertThat(result.getRequest() == holidayRequest).isTrue();
    }

    //addEmployee
    @Test
    void addEmployeeShouldThrowDepartmentMissingExceptionIfDepartmentIsNotFound() {
        Map<String, String> map = new HashMap<>();
        map.put("emp-date", "2020-07-14");
        Throwable throwable = catchThrowable(() -> hrService.addEmployee(map));
        assertThat(throwable).isInstanceOf(MissingDepartmentException.class);
    }

    @Test
    void addEmployeeShouldSaveTheEmployeeIfTheDepartmentIsPresent() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "1");
        map.put("address", "2");
        map.put("phone", "3");
        map.put("salary", "4");
        map.put("email", "5");
        map.put("emp-date", "");
        map.put("departmentId", "id");
        Department department = new Department();
        when(employeeRepository.save(any(Employee.class))).then(invocation -> invocation.getArguments()[0]);
        when(departmentRepository.findById(anyString())).thenReturn(java.util.Optional.of(department));

        try {
            Employee result = hrService.addEmployee(map);
            assertThat(result.getDepartment()==department).isTrue();
            assertThat(result.getEmail().equals("5")).isTrue();
            assertThat(result.getSalary() == 4).isTrue();
            assertThat(result.getTel().equals("3")).isTrue();
            assertThat(result.getAddress().equals("2")).isTrue();
            assertThat(result.getName().equals("1")).isTrue();
        } catch (Exception e) {
            fail("Exception interfered!");
        }

    }
}
