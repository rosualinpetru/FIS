package ro.go.redhomeserver.tom.models;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class DepartmentTest {
    @Test
    void checkConstructorAndGetters() {
        Department department = new Department("It");
        assertThat(department.getName().equals("It")).isTrue();
        assertThat(department.getId()).isNull();
    }

    @Test
    void checkNoArgsConstructor() {
        Department department = new Department();
        assertThat(department.getName()).isNull();
        assertThat(department.getId()).isNull();
    }

    @Test
    void checkSetter() {
        Department department = new Department();
        department.setName("It");
        assertThat(department.getName().equals("It")).isTrue();
    }

    @Test
    void checkEquals() {
        Department department1 = new Department("It");
        Department department2 = new Department("It");
        Department department3 = new Department("It1");
        assertThat(department1.equals(department2)).isTrue();
        assertThat(department1.equals(department3)).isFalse();
        assertThat(department1.equals(new Object())).isFalse();
        assertThat(department1.hashCode() == department2.hashCode()).isTrue();
    }
}
