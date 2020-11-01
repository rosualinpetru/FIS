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
        when(accountRepository.findByEmployee_Id(anyString())).thenReturn(java.util.Optional.of(new Account()));
        throwable = catchThrowable(() -> calendarService.loadHolidayRequestsOfTeamLeaderForCalendarById(""));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }
}
