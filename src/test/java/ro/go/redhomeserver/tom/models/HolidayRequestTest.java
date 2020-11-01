package ro.go.redhomeserver.tom.models;

import org.junit.jupiter.api.Test;
import ro.go.redhomeserver.tom.enums.RequestStatus;
import ro.go.redhomeserver.tom.enums.RequestType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class HolidayRequestTest {
    static Date parseDate(String date) {
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
        Date date3 = parseDate("2020-06-01");
        Account account1 = new Account();
        Account account2 = new Account();
        HolidayRequest holidayRequest = new HolidayRequest(requestType, requestStatus, "test", date1, date2, account1, account2);
        HolidayRequest holidayRequest2 = new HolidayRequest(requestType, requestStatus, "test", date1, date3, account1, account2);
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
        assertThat(holidayRequest2.getWorkingDays()).isZero();
    }

    @Test
    void checkSetter() {
        RequestType requestType = RequestType.Med;
        RequestStatus requestStatus = RequestStatus.accTl;
        Date date1 = new Date();
        Date date2 = new Date();
        Account account1 = new Account();
        Account account2 = new Account();
        Set<Feedback> requestFeedback = new HashSet<>();
        HolidayRequest holidayRequest = new HolidayRequest();
        holidayRequest.setType(requestType);
        holidayRequest.setStatus(requestStatus);
        holidayRequest.setDescription("test");
        holidayRequest.setStart(date1);
        holidayRequest.setEnd(date2);
        holidayRequest.setRequester(account1);
        holidayRequest.setDelegate(account2);
        holidayRequest.setId("1");
        holidayRequest.setRequestFeedback(requestFeedback);
        assertThat(holidayRequest.getType().equals(requestType)).isTrue();
        assertThat(holidayRequest.getStatus().equals(requestStatus)).isTrue();
        assertThat(holidayRequest.getDescription().equals("test")).isTrue();
        assertThat(holidayRequest.getStart().equals(date1)).isTrue();
        assertThat(holidayRequest.getEnd().equals(date2)).isTrue();
        assertThat(holidayRequest.getRequester().equals(account1)).isTrue();
        assertThat(holidayRequest.getDelegate().equals(account2)).isTrue();
        assertThat(holidayRequest.getId().equals("1")).isTrue();
    }

    @Test
    void checkEquals() {
        RequestType requestType = RequestType.Med;
        RequestStatus requestStatus = RequestStatus.accTl;
        Date date1 = parseDate("2020-06-01");
        Date date2 = parseDate("2020-06-03");
        Account account1 = new Account();
        Account account2 = new Account();
        HolidayRequest holidayRequest1 = new HolidayRequest(requestType, requestStatus, "test", date1, date2, account1, account2);
        HolidayRequest holidayRequest2 = new HolidayRequest(requestType, requestStatus, "test", date1, date2, account1, account2);
        HolidayRequest holidayRequest3 = new HolidayRequest(requestType, requestStatus, "test1", date1, date2, account1, account2);
        assertThat(holidayRequest1.equals(holidayRequest2)).isTrue();
        assertThat(holidayRequest1.equals(holidayRequest3)).isFalse();
        assertThat(holidayRequest1.equals(new Object())).isFalse();
        assertThat(holidayRequest1.hashCode() == holidayRequest2.hashCode()).isTrue();
    }
}
