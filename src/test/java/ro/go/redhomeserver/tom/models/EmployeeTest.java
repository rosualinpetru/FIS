package ro.go.redhomeserver.tom.models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTest {
    @Test
    void checkConstructorAndGetters() {
        Date date = new Date();
        Department department = new Department();
        Employee employee = new Employee("It", "address", "1111111111", 2000, "test@it.com", date, department);
        assertThat(employee.getName().equals("It")).isTrue();
        assertThat(employee.getAddress().equals("address")).isTrue();
        assertThat(employee.getTel().equals("1111111111")).isTrue();
        assertThat(employee.getSalary() == 2000).isTrue();
        assertThat(employee.getEmail().equals("test@it.com")).isTrue();
        assertThat(employee.getEmp_date().equals(date)).isTrue();
        assertThat(employee.getDepartment().equals(department)).isTrue();
        assertThat(employee.getId()).isNull();
        assertThat(employee.getAccount()).isNull();
    }

    @Test
    void checkNoArgsConstructor() {
        Employee employee = new Employee();
        assertThat(employee.getName()).isNull();
        assertThat(employee.getAddress()).isNull();
        assertThat(employee.getTel()).isNull();
        assertThat(employee.getSalary() == 0).isTrue();
        assertThat(employee.getEmail()).isNull();
        assertThat(employee.getEmp_date()).isNull();
        assertThat(employee.getDepartment()).isNull();
        assertThat(employee.getId()).isNull();
    }

    @Test
    void checkSetter() {
        Date date = new Date();
        Department department = new Department();
        Employee employee = new Employee();
        employee.setName("It");
        employee.setAddress("address");
        employee.setTel("1111111111");
        employee.setSalary(2000);
        employee.setEmail("test@it.com");
        employee.setEmp_date(date);
        employee.setDepartment(department);
        assertThat(employee.getName().equals("It")).isTrue();
        assertThat(employee.getAddress().equals("address")).isTrue();
        assertThat(employee.getTel().equals("1111111111")).isTrue();
        assertThat(employee.getSalary() == 2000).isTrue();
        assertThat(employee.getEmail().equals("test@it.com")).isTrue();
        assertThat(employee.getEmp_date().equals(date)).isTrue();
        assertThat(employee.getDepartment().equals(department)).isTrue();
    }

    @Test
    void checkEquals() {
        Date date = new Date();
        Department department = new Department();
        Employee employee1 = new Employee("It", "address", "1111111111", 2000, "test@it.com", date, department);
        Employee employee2 = new Employee("It", "address", "1111111111", 2000, "test@it.com", date, department);
        Employee employee3 = new Employee("It1", "address", "1111111111", 2000, "test@it.com", date, department);
        assertThat(employee1.equals(employee2)).isTrue();
        assertThat(employee1.equals(employee3)).isFalse();
        assertThat(employee1.equals(new Object())).isFalse();
        assertThat(employee1.hashCode() == employee2.hashCode()).isTrue();
    }
}
