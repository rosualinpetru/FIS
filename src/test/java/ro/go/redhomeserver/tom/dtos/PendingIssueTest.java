package ro.go.redhomeserver.tom.dtos;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class PendingIssueTest {
    @Test
    void checkConstructorsAndGetters (){


       PendingIssue pendingIssue = new PendingIssue("id", "test", "test","test");

        assertThat(pendingIssue.getId().equals("id")).isTrue();
        assertThat(pendingIssue.getDepartmentName().equals("test")).isTrue();
        assertThat(pendingIssue.getDescription().equals("test")).isTrue();
        assertThat(pendingIssue.getName().equals("test")).isTrue();

    }
    @Test
    void checkSetters(){
        PendingIssue pendingIssue = new PendingIssue("id", "test", "test","test");


        pendingIssue.setDepartmentName("test1");
        pendingIssue.setDescription("test2");
        pendingIssue.setId("id1");
        pendingIssue.setName("test3");

        assertThat(pendingIssue.getId().equals("id1")).isTrue();
        assertThat(pendingIssue.getDepartmentName().equals("test1")).isTrue();
        assertThat(pendingIssue.getDescription().equals("test2")).isTrue();
        assertThat(pendingIssue.getName().equals("test3")).isTrue();
    }
}
