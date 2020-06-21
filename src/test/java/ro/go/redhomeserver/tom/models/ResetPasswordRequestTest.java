package ro.go.redhomeserver.tom.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class ResetPasswordRequestTest {
    @Test
    void checkConstructorAndGetters() {
        Account account = new Account();
        Date date = new Date();
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(account, "test", date);
        assertThat(resetPasswordRequest.getAccount().equals(account)).isTrue();
        assertThat(resetPasswordRequest.getToken().equals("test")).isTrue();
        assertThat(resetPasswordRequest.getExpirationDate().equals(date)).isTrue();
        assertThat(resetPasswordRequest.getId()).isNull();
    }

    @Test
    void checkNoArgsConstructor() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        assertThat(resetPasswordRequest.getAccount()).isNull();
        assertThat(resetPasswordRequest.getToken()).isNull();
        assertThat(resetPasswordRequest.getExpirationDate()).isNull();
        assertThat(resetPasswordRequest.getId()).isNull();
    }

    @Test
    void checkSetter() {
        Account account = new Account();
        Date date = new Date();
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setAccount(account);
        resetPasswordRequest.setToken("test");
        resetPasswordRequest.setExpirationDate(date);
        resetPasswordRequest.setId("2");
        assertThat(resetPasswordRequest.getAccount().equals(account)).isTrue();
        assertThat(resetPasswordRequest.getToken().equals("test")).isTrue();
        assertThat(resetPasswordRequest.getExpirationDate().equals(date)).isTrue();
    }

    @Test
    void checkEquals() {
        Account account = new Account();
        Date date = new Date();
        ResetPasswordRequest resetPasswordRequest1 = new ResetPasswordRequest(account, "test", date);
        ResetPasswordRequest resetPasswordRequest2 = new ResetPasswordRequest(account, "test", date);
        ResetPasswordRequest resetPasswordRequest3 = new ResetPasswordRequest(account, "test1", date);
        assertThat(resetPasswordRequest1.equals(resetPasswordRequest2)).isTrue();
        assertThat(resetPasswordRequest1.equals(resetPasswordRequest3)).isFalse();
        assertThat(resetPasswordRequest1.equals(new Object())).isFalse();
        assertThat(resetPasswordRequest1.hashCode() == resetPasswordRequest2.hashCode()).isTrue();
    }
}
