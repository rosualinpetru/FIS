package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.enums.RequestStatus;
import ro.go.redhomeserver.tom.enums.RequestType;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CalendarServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private HolidayRequestRepository holidayRequestRepository;

    @InjectMocks
    private CalendarService calendarService;

    //loadHolidayRequestsOfTeamLeaderForCalendarById - the only method that need to be tested
    //as it is dependent on the others

    @Test
    void loadShouldThrowUserNotFoundExceptionIfAccountNotFoundByIdOrUsername() {
        Throwable throwable = catchThrowable(() -> calendarService.loadHolidayRequestsOfTeamLeaderForCalendarById(""));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        when(accountRepository.findById(anyString())).thenReturn(java.util.Optional.of(new Account()));
        throwable = catchThrowable(() -> calendarService.loadHolidayRequestsOfTeamLeaderForCalendarById(""));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void forHolidayRequestsTharAreOnlyMineNoCalendarEventNameDiffersFromMeAndTheOthersShouldHaveTheTitleNameOfEmployee() {
        ArrayList<HolidayRequest> holidayRequests = new ArrayList<>();
        Account me = new Account();
        me.setUsername("arosu");
        HolidayRequest hr;
        for (int i = 0; i < 10; i++) {
            hr = new HolidayRequest();
            hr.setRequester(me);
            hr.setType(RequestType.values()[i % 4]);
            if (i % 2 == 0)
                hr.setStatus(RequestStatus.accTl);
            else
                hr.setStatus(RequestStatus.decTL);
            holidayRequests.add(hr);
        }

        ArrayList<HolidayRequest> holidayRequests1 = new ArrayList<>();
        hr = new HolidayRequest();
        Account account = new Account();
        Employee employee = new Employee();
        employee.setName("name");
        account.setEmployee(employee);
        hr.setRequester(account);
        hr.setType(RequestType.Med);
        holidayRequests1.add(hr);

        when(accountRepository.findById(anyString())).thenReturn(java.util.Optional.of(me));
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(me));
        when(holidayRequestRepository.findAllByRequesterAndStatus(any(Account.class), any(RequestStatus.class))).thenReturn(holidayRequests);
        when(holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(any(Account.class), any(RequestStatus.class))).thenReturn(holidayRequests1);

        try {
            List<CalendarEvent> result = calendarService.loadHolidayRequestsOfTeamLeaderForCalendarById("");
            assertThat(result.size() == 11).isTrue();
            for (CalendarEvent e : result.subList(1, result.size())) {
                assertThat(e.getTitle().equals("ME")).isTrue();
            }
            assertThat(result.get(0).getTitle().equals("name")).isTrue();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }
}
