package ro.go.redhomeserver.tom.models;


import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountTest {
    @Test
    void checkConstructorAndGetters() {
        Employee employee = new Employee();
        Account teamLeader = new Account();
        Account account = new Account("It", "test", "it", employee, teamLeader, 20);
        assertThat(account.getUsername().equals("It")).isTrue();
        assertThat(account.getPassword().equals("test")).isTrue();
        assertThat(account.getSalt().equals("it")).isTrue();
        assertThat(account.getEmployee().equals(employee)).isTrue();
        assertThat(account.getTeamLeader().equals(teamLeader)).isTrue();
        assertThat(account.getRemainingDays() == 20).isTrue();
        assertThat(account.getId()).isNull();
        assertThat(account.getIssueRequests()).isEmpty();
        assertThat(account.getGivenFeedback()).isEmpty();
        assertThat(account.getSentHolidayRequests()).isEmpty();
        assertThat(account.getDelegatedHolidayRequests()).isEmpty();
        assertThat(account.getResetPasswordRequests()).isEmpty();
        assertThat(account.getMembers()).isEmpty();
        assertThat(account.isActivated()).isFalse();
    }

    @Test
    void checkNoArgsConstructor() {
        Account account = new Account();
        assertThat(account.getUsername()).isNull();
        assertThat(account.getPassword()).isNull();
        assertThat(account.getSalt()).isNull();
        assertThat(account.getEmployee()).isNull();
        assertThat(account.getTeamLeader()).isNull();
        assertThat(account.getRemainingDays() == 0).isTrue();
        assertThat(account.getId()).isNull();
    }

    @Test
    void checkSetter() {
        Employee employee = new Employee();
        Account teamLeader = new Account();
        Set<HolidayRequest> delegatedHolidayRequests = new HashSet<>();
        Set<IssueRequest> issueRequests = new HashSet<>();
        Set<Feedback> givenFeedback = new HashSet<>();
        Set<HolidayRequest> sentHolidayRequests = new HashSet<>();
        Set<ResetPasswordRequest> resetPasswordRequests = new HashSet<>();
        Account account = new Account();
        account.setUsername("It");
        account.setPassword("test");
        account.setSalt("it");
        account.setEmployee(employee);
        account.setTeamLeader(teamLeader);
        account.setRemainingDays(20);
        account.setDelegatedHolidayRequests(delegatedHolidayRequests);
        account.setIssueRequests(issueRequests);
        account.setGivenFeedback(givenFeedback);
        account.setSentHolidayRequests(sentHolidayRequests);
        account.setResetPasswordRequests(resetPasswordRequests);
        assertThat(account.getUsername().equals("It")).isTrue();
        assertThat(account.getPassword().equals("test")).isTrue();
        assertThat(account.getSalt().equals("it")).isTrue();
        assertThat(account.getEmployee().equals(employee)).isTrue();
        assertThat(account.getTeamLeader().equals(teamLeader)).isTrue();
        assertThat(account.getRemainingDays() == 20).isTrue();
    }

    @Test
    void checkEquals() {
        Employee employee = new Employee();
        Account teamLeader = new Account();
        Account account1 = new Account("It", "test", "it", employee, teamLeader, 20);
        Account account2 = new Account("It", "test", "it", employee, teamLeader, 20);
        Account account3 = new Account("It1", "test", "it", employee, teamLeader, 20);
        assertThat(account1.equals(account2)).isTrue();
        assertThat(account1.equals(account3)).isFalse();
        assertThat(account1.equals(new Object())).isFalse();
        assertThat(account1.hashCode() == account2.hashCode()).isTrue();

    }
}
