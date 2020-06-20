package ro.go.redhomeserver.tom.emails;

import org.junit.jupiter.api.Test;
import ro.go.redhomeserver.tom.models.Account;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailDataTest {
    @Test
    void checkConstructorAndGetters() {
        Account account = new Account();
        EmailData emailData = new EmailData(account,"test");
        assertThat(emailData.getTo().equals(account)).isTrue();
        assertThat(emailData.getSubject().equals("test")).isTrue();
    }

}

