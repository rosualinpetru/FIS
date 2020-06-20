package ro.go.redhomeserver.tom.emails;

import org.junit.jupiter.api.Test;
import ro.go.redhomeserver.tom.models.Account;

import static org.assertj.core.api.Assertions.assertThat;

public class HolidayStatusUpdateEmailTest {
    @Test
    void checkConstructorAndGetters(){
        Account account = new Account();
        HolidayStatusUpdateEmail holidayStatusUpdateEmail= new HolidayStatusUpdateEmail(account,"test");
        assertThat(holidayStatusUpdateEmail.getTo().equals(account)).isTrue();
       // assertThat(holidayStatusUpdateEmail.getSubject().equals("test")).isTrue();
    }
}
