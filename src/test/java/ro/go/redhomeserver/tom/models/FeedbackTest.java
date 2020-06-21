package ro.go.redhomeserver.tom.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedbackTest {
    @Test
    void checkConstructorAndGetters() {
        HolidayRequest holidayRequest = new HolidayRequest();
        Account account = new Account();
        Feedback feedback = new Feedback(holidayRequest, "test", account);
        assertThat(feedback.getRequest().equals(holidayRequest)).isTrue();
        assertThat(feedback.getDescription().equals("test")).isTrue();
        assertThat(feedback.getReporter().equals(account)).isTrue();
        assertThat(feedback.getId()).isNull();
    }

    @Test
    void checkNoArgsConstructor() {
        Feedback feedback = new Feedback();
        assertThat(feedback.getRequest()).isNull();
        assertThat(feedback.getDescription()).isNull();
        assertThat(feedback.getReporter()).isNull();
        assertThat(feedback.getId()).isNull();
    }

    @Test
    void checkEquals() {
        HolidayRequest holidayRequest = new HolidayRequest();
        Account account = new Account();
        Feedback feedback1 = new Feedback(holidayRequest, "test", account);
        Feedback feedback2 = new Feedback(holidayRequest, "test", account);
        Feedback feedback3 = new Feedback(holidayRequest, "test1", account);
        assertThat(feedback1.equals(feedback2)).isTrue();
        assertThat(feedback1.equals(feedback3)).isFalse();
        assertThat(feedback1.equals(new Object())).isFalse();
        assertThat(feedback1.hashCode() == feedback2.hashCode()).isTrue();
    }

    @Test
    void checkSetter() {
        HolidayRequest holidayRequest = new HolidayRequest();
        Account account = new Account();
        Feedback feedback = new Feedback();
        feedback.setRequest(holidayRequest);
        feedback.setDescription("test");
        feedback.setReporter(account);
        feedback.setId("1");
        assertThat(feedback.getRequest().equals(holidayRequest)).isTrue();
        assertThat(feedback.getDescription().equals("test")).isTrue();
        assertThat(feedback.getReporter().equals(account)).isTrue();
        assertThat(feedback.getId().equals("1")).isTrue();
    }
}
