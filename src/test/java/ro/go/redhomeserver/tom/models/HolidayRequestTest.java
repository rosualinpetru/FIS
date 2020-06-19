package ro.go.redhomeserver.tom.models;

import org.junit.jupiter.api.Test;
import ro.go.redhomeserver.tom.enums.RequestStatus;
import ro.go.redhomeserver.tom.enums.RequestType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;

public class HolidayRequestTest {
    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Test
    void checkConstructorAndGetters() {
        RequestType requestType = RequestType.Med;
        RequestStatus requestStatus = RequestStatus.accTl;
        Date date1 = parseDate("2020-06-01");
        Date date2 = parseDate("2020-06-03");
        Account account1 = new Account();
        Account account2 = new Account();
        HolidayRequest holidayRequest = new HolidayRequest(requestType,requestStatus,"test",date1,date2,account1,account2);
        assertThat(holidayRequest.getType().equals(requestType)).isTrue();
        assertThat(holidayRequest.getStatus().equals(requestStatus)).isTrue();
        assertThat(holidayRequest.getDescription().equals("test")).isTrue();
        assertThat(holidayRequest.getStart().equals(date1)).isTrue();
        assertThat(holidayRequest.getEnd().equals(date2)).isTrue();
        assertThat(holidayRequest.getRequester().equals(account1)).isTrue();
        assertThat(holidayRequest.getDelegate().equals(account2)).isTrue();
        assertThat(holidayRequest.getId()).isNull();
        assertThat(holidayRequest.getUploadedFile()).isNull();
        assertThat(holidayRequest.getRequestFeedback().isEmpty());
        assertThat(holidayRequest.getWorkingDays() == 2).isTrue();
    }

    @Test
    void checkSetter() {
        RequestType requestType = RequestType.Med;
        RequestStatus requestStatus = RequestStatus.accTl;
        Date date1 = new Date();
        Date date2 = new Date();
        Account account1 = new Account();
        Account account2 = new Account();
        HolidayRequest holidayRequest = new HolidayRequest();
        holidayRequest.setType(requestType);
        holidayRequest.setStatus(requestStatus);
        holidayRequest.setDescription("test");
        holidayRequest.setStart(date1);
        holidayRequest.setEnd(date2);
        holidayRequest.setRequester(account1);
        holidayRequest.setDelegate(account2);
        assertThat(holidayRequest.getType().equals(requestType)).isTrue();
        assertThat(holidayRequest.getStatus().equals(requestStatus)).isTrue();
        assertThat(holidayRequest.getDescription().equals("test")).isTrue();
        assertThat(holidayRequest.getStart().equals(date1)).isTrue();
        assertThat(holidayRequest.getEnd().equals(date2)).isTrue();
        assertThat(holidayRequest.getRequester().equals(account1)).isTrue();
        assertThat(holidayRequest.getDelegate().equals(account2)).isTrue();
    }
}
