package ro.go.redhomeserver.tom.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class RequestStatusTest {
    @Test
    public void checkRequestStatus() {
        assertThat(RequestStatus.accTl.getDisplayValue().equals("Accepted")).isTrue();
        assertThat(RequestStatus.sentTL.getDisplayValue().equals("Pending")).isTrue();
        assertThat(RequestStatus.decTL.getDisplayValue().equals("Declined")).isTrue();

    }
}
