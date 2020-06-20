package ro.go.redhomeserver.tom.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestTypeTest {
    @Test
    public void checkRequestType() {
        assertThat(RequestType.Med.getDisplayValue().equals("Medical")).isTrue();
        assertThat(RequestType.Rel.getDisplayValue().equals("Rest")).isTrue();
        assertThat(RequestType.Fam.getDisplayValue().equals("Family situation")).isTrue();
        assertThat(RequestType.Hof.getDisplayValue().equals("Home Office")).isTrue();

    }
}
