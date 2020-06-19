package ro.go.redhomeserver.tom.models;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueRequestTest {
    @Test
    void checkConstructorAndGetters() {
        Account account = new Account();
        IssueRequest issueRequest = new IssueRequest("test",account);
        assertThat(issueRequest.getDescription().equals("test")).isTrue();
        assertThat(issueRequest.getAccount().equals(account)).isTrue();
        assertThat(issueRequest.getId()).isNull();
    }

    @Test
    void checkSetter() {
        Account account = new Account();
        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setAccount(account);
        issueRequest.setDescription("test");
        assertThat(issueRequest.getDescription().equals("test")).isTrue();
        assertThat(issueRequest.getAccount().equals(account)).isTrue();
    }

    @Test
    void checkEquals() {
        Account account = new Account();
        IssueRequest issueRequest1 = new IssueRequest("test",account);
        IssueRequest issueRequest2 = new IssueRequest("test",account);
        assertThat(issueRequest1.equals(issueRequest2)).isTrue();
    }
}
