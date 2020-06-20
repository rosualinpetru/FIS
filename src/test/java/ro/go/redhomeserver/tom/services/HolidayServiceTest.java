package ro.go.redhomeserver.tom.services;

import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailPreparationException;
import org.springframework.web.multipart.MultipartFile;
import ro.go.redhomeserver.tom.dtos.CalendarEvent;
import ro.go.redhomeserver.tom.emails.EmailData;
import ro.go.redhomeserver.tom.enums.RequestStatus;
import ro.go.redhomeserver.tom.enums.RequestType;
import ro.go.redhomeserver.tom.exceptions.NotEnoughDaysException;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.HolidayRequest;
import ro.go.redhomeserver.tom.models.UploadedFile;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.HolidayRequestRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HolidayServiceTest {
    @Mock
    private EmailService emailService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private HolidayRequestRepository holidayRequestRepository;
    @Mock
    private UploadedFileService uploadedFileService;

    @InjectMocks
    private HolidayService holidayService;

    // loadHolidayRequestsBasedOnUserAndStatus + all methods based on it (3)
    @Test
    void loadHolidayRequestsBasedOnUserAndStatus_should_ThrowUserNotFoundException_UsernameNotFound() {
        Throwable throwable = catchThrowable(() -> holidayService.loadMyAcceptedHolidayRequests(anyString()));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        throwable = catchThrowable(() -> holidayService.loadMyDeclinedHolidayRequests(anyString()));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
        throwable = catchThrowable(() -> holidayService.loadMyPendingHolidayRequests(anyString()));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldReturnResultOfHolidayRequestRepositoryQueryAccountAndStatus() {
        List<HolidayRequest> result = new ArrayList<>();
        when(holidayRequestRepository.findAllByRequesterAndStatus(any(Account.class), (any(RequestStatus.class)))).thenReturn(result);
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(new Account()));
        try {
            assertThat(holidayService.loadMyAcceptedHolidayRequests("") == result).isTrue();
            assertThat(holidayService.loadMyDeclinedHolidayRequests("") == result).isTrue();
            assertThat(holidayService.loadMyPendingHolidayRequests("") == result).isTrue();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    //getFileOfRequest
    @Test
    void shouldReturnResultOfUploadedFileRepositoryQuery() {
        assertThat(holidayService.getFileOfRequest(null)).isNull();
        UploadedFile uf = new UploadedFile();
        when(uploadedFileService.getFileByRequestId(anyString())).thenReturn(uf);
        assertThat(holidayService.getFileOfRequest("")==uf).isTrue();
    }

    //loadPendingHolidayRequestsForATeamLeader
    @Test
    void loadPendingHolidayRequestsForATeamLeader_should_ThrowUserNotFoundException_UsernameNotFound() {
        Throwable throwable = catchThrowable(() -> holidayService.loadPendingHolidayRequestsForATeamLeader(anyString()));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldReturnResultOfHolidayRequestRepositoryQueryTeamLeaderAndPending() {
        List<HolidayRequest> holidayRequests = new ArrayList<>();
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(new Account()));
        when(holidayRequestRepository.findAllByRequester_TeamLeaderAndStatus(any(Account.class), any(RequestStatus.class))).thenReturn(holidayRequests);

        try {
            assertThat(holidayService.loadPendingHolidayRequestsForATeamLeader("")).isNotNull();
            assertThat(holidayService.loadPendingHolidayRequestsForATeamLeader("")==holidayRequests).isTrue();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    //updateStatusOfHolidayRequest
    @Test
    void updateStatusOfHolidayRequestShouldNotSendEmailReturnNullIfRequestNotFound() {
        try {
            assertThat(holidayService.updateStatusOfHolidayRequest(null, null)).isNull();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
        verify(emailService, times(0)).sendEmail(any(EmailData.class));
    }

    @Test
    void updateStatusOfHolidayRequestShouldThrowSystemExceptionIfEmailServiceThrowsMailException() {
        HolidayRequest holidayRequest = new HolidayRequest();
        when(holidayRequestRepository.findById(anyString())).thenReturn(java.util.Optional.of(holidayRequest));
        doThrow(new MailPreparationException("Failed")).when(emailService).sendEmail(any(EmailData.class));
        Throwable throwable = catchThrowable(() ->holidayService.updateStatusOfHolidayRequest("", ""));
        assertThat(throwable).isInstanceOf(SystemException.class);
    }

    @Test
    void updateStatusOfHolidayRequestShouldSendAnEmailAndChangeStatusOfRequestActionAccepted() {
        HolidayRequest holidayRequest = new HolidayRequest();
        when(holidayRequestRepository.findById(anyString())).thenReturn(java.util.Optional.of(holidayRequest));
        when(holidayRequestRepository.save(any(HolidayRequest.class))).then(invocation -> invocation.getArguments()[0]);
        try {
            HolidayRequest result = holidayService.updateStatusOfHolidayRequest("", "acc");
            assertThat(result.getStatus().equals(RequestStatus.accTl)).isTrue();
            verify(emailService, times(1)).sendEmail(any(EmailData.class));
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    @Test
    void updateStatusOfHolidayRequestShouldSendAnEmailAndChangeStatusOfRequestActionDeclinedAndRestoreWorkingDays() {
        HolidayRequest holidayRequest = new HolidayRequest();
        Account requester = new Account();

        Calendar cal = Calendar.getInstance();
        holidayRequest.setStart(cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, +7);
        holidayRequest.setEnd(cal.getTime());
        requester.setRemainingDays(21);

        holidayRequest.setRequester(requester);

        when(holidayRequestRepository.findById(anyString())).thenReturn(java.util.Optional.of(holidayRequest));
        when(holidayRequestRepository.save(any(HolidayRequest.class))).then(invocation -> invocation.getArguments()[0]);
        try {
            HolidayRequest result = holidayService.updateStatusOfHolidayRequest("", "dec");
            assertThat(result.getStatus().equals(RequestStatus.decTL)).isTrue();
            assertThat(result.getRequester().getRemainingDays()==26).isTrue();
            verify(emailService, times(1)).sendEmail(any(EmailData.class));
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    //addHolidayRequest
    @Test
    void addHolidayRequestShouldThrowUserNotFoundExceptionIfUserNotFound() {
        Throwable throwable = catchThrowable(() -> holidayService.addHolidayRequest(null, null, null));
        assertThat(throwable).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void addHolidayRequestShouldThrowParseExceptionForBadDateFormat() {
        Map<String, String> form = new HashMap<>();
        form.put("startDate", "badFormat");
        form.put("endDate", "anotherBadFormat");
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(new Account()));
        Throwable throwable = catchThrowable(() -> holidayService.addHolidayRequest("", form, null));
        assertThat(throwable).isInstanceOf(ParseException.class);
    }

    @Test
    void addHolidayRequestShouldSaveRequestWithSentTLStatusForPresentTLWithNullDelegateForDelegateIdNotSetTypeFamRequest() {
        Map<String, String> form = new HashMap<>();
        form.put("startDate", "2020-07-14");
        form.put("endDate", "2020-07-19");
        form.put("requestTypeId", "Fam");
        Account account = new Account();
        account.setTeamLeader(new Account());
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(new Account()));
        when(holidayRequestRepository.save(any(HolidayRequest.class))).then(invocation -> invocation.getArguments()[0]);
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            HolidayRequest result = holidayService.addHolidayRequest("", form, null);
            assertThat(result.getDelegate()).isNull();
            assertThat(result.getType().equals(RequestType.Fam)).isTrue();
            assertThat(result.getStart().equals(format.parse("2020-07-14")));
            assertThat(result.getStatus().equals(RequestStatus.sentTL));
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    @Test
    void addHolidayRequestShouldSaveRequestWithAccTLStatusForMissingTLWithNotNullDelegateAndStoreAFileTypeMed() {
        Map<String, String> form = new HashMap<>();
        form.put("startDate", "2020-07-14");
        form.put("endDate", "2020-07-19");
        form.put("requestTypeId", "Med");
        form.put("delegateId", "id");
        Account account = new Account();
        Account delegate = new Account();
        account.setTeamLeader(new Account());
        when(accountRepository.findById("id")).thenReturn(Optional.of(delegate));
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(new Account()));
        when(holidayRequestRepository.save(any(HolidayRequest.class))).then(invocation -> invocation.getArguments()[0]);
        try {
            HolidayRequest result = holidayService.addHolidayRequest("", form, new MultipartFile() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getOriginalFilename() {
                    return null;
                }

                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public long getSize() {
                    return 0;
                }

                @Override
                public byte[] getBytes() {
                    return new byte[0];
                }

                @Override
                public InputStream getInputStream() {
                    return null;
                }

                @Override
                public void transferTo(File dest) throws IllegalStateException {

                }
            });
            assertThat(result.getDelegate()).isNotNull();
            assertThat(result.getType().equals(RequestType.Med)).isTrue();
            assertThat(result.getStatus().equals(RequestStatus.accTl)).isTrue();
            verify(uploadedFileService, times(1)).storeFile(any(MultipartFile.class), any(HolidayRequest.class));
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    @Test
    void addHolidayRequestShouldThrowNotEnoughDaysExceptionTypeRel() {
        Map<String, String> form = new HashMap<>();
        form.put("startDate", "2020-07-14");
        form.put("endDate", "2020-07-21");
        form.put("requestTypeId", "Rel");
        Account account = new Account();
        account.setRemainingDays(2);
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        Throwable throwable = catchThrowable(() -> holidayService.addHolidayRequest("", form, null));
        assertThat(throwable).isInstanceOf(NotEnoughDaysException.class);
    }

    @Test
    void addHolidayRequestShouldSubtractRemainingDaysTypeRel() {
        Map<String, String> form = new HashMap<>();
        form.put("startDate", "2020-07-14");
        form.put("endDate", "2020-07-21");
        form.put("requestTypeId", "Rel");
        Account account = new Account();
        account.setRemainingDays(31);
        when(accountRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(account));
        when(holidayRequestRepository.save(any(HolidayRequest.class))).then(invocation -> invocation.getArguments()[0]);
        try {
            HolidayRequest result = holidayService.addHolidayRequest("", form, null);
            assertThat(result.getType().equals(RequestType.Rel)).isTrue();
            assertThat(result.getRequester().getRemainingDays()==26).isTrue();
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }
}
