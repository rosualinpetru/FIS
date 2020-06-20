package ro.go.redhomeserver.tom.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.models.ResetPasswordRequest;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;
import ro.go.redhomeserver.tom.repositories.ResetPasswordRequestRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClearDataServiceTest {

    @Mock
    private ResetPasswordRequestRepository resetPasswordRequestRepository;
    @Mock
    private HolidayRequestRepository holidayRequestRepository;

    @InjectMocks
    private ClearDataService clearDataService;

    @Test
    void clearDataShouldDeleteAllExpiredResetPasswordRequestsAndAllHolidayRequestsWhichEndedOneYearAgo() {
        ArrayList<ResetPasswordRequest> resetPasswordRequests = new ArrayList<>();
        ArrayList<HolidayRequest> holidayRequests = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.YEAR, -1);
        Date yearAgo = cal.getTime();
        cal.add(Calendar.YEAR, 1);

        ResetPasswordRequest rpr;
        rpr = new ResetPasswordRequest();
        rpr.setExpirationDate(cal.getTime());
        resetPasswordRequests.add(rpr);

        rpr = new ResetPasswordRequest();
        cal.add(Calendar.MINUTE, +5);
        rpr.setExpirationDate(cal.getTime());
        resetPasswordRequests.add(rpr);

        rpr = new ResetPasswordRequest();
        cal.add(Calendar.MINUTE, -10);
        rpr.setExpirationDate(cal.getTime());
        resetPasswordRequests.add(rpr);

        HolidayRequest hr;
        hr = new HolidayRequest();
        hr.setEnd(cal.getTime());
        holidayRequests.add(hr);

        hr = new HolidayRequest();
        cal.add(Calendar.YEAR, -2);
        hr.setEnd(cal.getTime());
        holidayRequests.add(hr);

        hr = new HolidayRequest();
        cal.add(Calendar.YEAR, +4);
        hr.setEnd(cal.getTime());
        holidayRequests.add(hr);

        doAnswer(invocation -> resetPasswordRequests.removeIf(i->i.getExpirationDate().compareTo(now)>=0)).when(resetPasswordRequestRepository).deleteAllByExpirationDateIsLessThanEqual(any(Date.class));
        doAnswer(invocation -> holidayRequests.removeIf(i->i.getEnd().before(yearAgo))).when(holidayRequestRepository).deleteAllByEndIsLessThan(any(Date.class));

        clearDataService.clearData();

        assertThat(resetPasswordRequests.size()==1).isTrue();
        assertThat(holidayRequests.size()==2).isTrue();
    }
}
