package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.dtos.PendingIssue;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IssueRequestServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private IssueRequestRepository issueRequestRepository;

    @InjectMocks
    private IssueRequestService issueRequestService;

    //addIssueRequest
    @Test
    void should_ThrowUserNotFoundException_NullUsername() {
        Throwable throwable = catchThrowable(() -> issueRequestService.addIssueRequest(null, null));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void checkIfIssueRequestIsSavedCorrectly() {
        Account account = new Account();
        account.setId("id");
        account.setUsername("username");
        when(accountRepository.findByUsername("username")).thenReturn(java.util.Optional.of(account));
        when(issueRequestRepository.save(any(IssueRequest.class))).then(invocation -> invocation.getArguments()[0]);

        HashMap<String, String> params = new HashMap<>();
        params.put("description", "myDescription");
        try {
            IssueRequest request = issueRequestService.addIssueRequest("username", params);
            assertThat(request.getAccount().equals(account)).isTrue();
            assertThat(request.getDescription().equals("myDescription")).isTrue();
        } catch (Exception e) {
            fail("Exception interfered!");
        }

    }

    //deleteIssueRequestById
    @Test
    void checkIfDeleteRequestByIdWorksIfRequestFoundOrNot() {
        List<IssueRequest> issueRequests = new ArrayList<>();
        IssueRequest ir;
        for (int i = 0; i < 10; i++) {
            ir = new IssueRequest();
            ir.setId("" + i);
            issueRequests.add(ir);
        }

        doAnswer(invocation -> issueRequests.removeIf(i -> i.getId().equals(invocation.getArguments()[0]))).when(issueRequestRepository).deleteById(anyString());

        issueRequestService.deleteIssueRequestById("11");

        assertThat(issueRequests.size() == 10).isTrue();
        issueRequestService.deleteIssueRequestById("3");
        assertThat(issueRequests.size() == 9).isTrue();
        assertThat(issueRequests.get(3).getId().equals("4")).isTrue();
    }

    //loadAllPendingIssueRequests
    @Test
    void checkIfAllPendingRequestsAreLoadedAndIfRequesterNullThenITSERVICEName() {
        List<IssueRequest> issueRequests = new ArrayList<>();
        IssueRequest ir;
        Account account;
        Employee employee;
        Department department;
        for (int i = 0; i < 5; i++) {
            ir = new IssueRequest();
            ir.setId(""+i);
            issueRequests.add(ir);

            ir = new IssueRequest();
            account = new Account();
            employee = new Employee();
            department = new Department("Dep" + i);
            department.setId("id"+i);
            employee.setName("Requester" + i);
            employee.setDepartment(department);
            account.setEmployee(employee);
            ir.setAccount(account);
            ir.setDescription("D" + i);
            issueRequests.add(ir);
        }

        when(issueRequestRepository.findAll()).then(invocation -> {
            ArrayList<IssueRequest> temp = new ArrayList<>(issueRequests);
            Collections.copy(temp,issueRequests);
            return temp;
        });

        List<PendingIssue> result = issueRequestService.loadAllPendingIssueRequests();

        assertThat(result.size()==10).isTrue();

        for (int i = 0; i < 5; i++)
            assertThat(result.contains(new PendingIssue(null, "Dep" + i, "Requester" + i, "D" + i))).isTrue();

        for (int i = 0; i < 5; i++)
            assertThat(result.contains(new PendingIssue(""+i, "IT", "IT-SERVICE", null))).isTrue();
    }
}
