package ro.go.redhomeserver.tom.dtos;

import org.junit.jupiter.api.Test;
import ro.go.redhomeserver.tom.models.Department;

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
    @Test
    void checkEquals() {
        PendingIssue pendingIssue1 = new PendingIssue("id", "test", "test","test");
        PendingIssue pendingIssue2 = new PendingIssue("id", "test", "test","test");
        PendingIssue pendingIssue3 = new PendingIssue("id1", "test1", "test1","test1");
        assertThat(pendingIssue1.equals(pendingIssue2)).isTrue();
        assertThat(pendingIssue1.equals(pendingIssue3)).isFalse();
        assertThat(pendingIssue1.equals(new Object())).isFalse();
        assertThat(pendingIssue1.hashCode() == pendingIssue2.hashCode()).isTrue();
    }
}
