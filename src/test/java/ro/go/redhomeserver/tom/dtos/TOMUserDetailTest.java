package ro.go.redhomeserver.tom.dtos;


import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;

import static org.assertj.core.api.Assertions.assertThat;

public class TOMUserDetailTest {


    @Test
    public void TOMUserDetailsConstructorShouldCreateInstanceWithAuthoritiesITAndActiveAndAdmin() {
        Employee empl1 = new Employee();
        Department department = new Department("IT");
        empl1.setDepartment(department);
        Account account = new Account();
        account.setEmployee(empl1);
        account.setActivated(true);
        TOMUserDetails tomUserDetails = new TOMUserDetails(account);

        assertThat(tomUserDetails.isActivated()).isTrue();
        assertThat(tomUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("IT"))).isTrue();
        assertThat(tomUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ACTIVATED"))).isTrue();
        assertThat(tomUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))).isTrue();
    }


    @Test
    public void TOMUserDetailsGettersShouldReturnCorrectValues() {
        Employee empl1 = new Employee();
        Department department = new Department("IT");
        empl1.setDepartment(department);
        Account account = new Account();
        account.setId("test");
        account.setPassword("pass");
        account.setUsername("user");
        account.setEmployee(empl1);
        account.setTeamLeader(new Account());

        TOMUserDetails tomUserDetails = new TOMUserDetails(account);

        assertThat(account.isActivated()).isFalse();
        assertThat(tomUserDetails.getUsername().equals("user")).isTrue();
        assertThat(tomUserDetails.getPassword().equals("pass")).isTrue();
        assertThat(tomUserDetails.getId().equals("test")).isTrue();
        assertThat(tomUserDetails.isAccountNonLocked()).isTrue();
        assertThat(tomUserDetails.isAccountNonExpired()).isTrue();
        assertThat(tomUserDetails.isCredentialsNonExpired()).isTrue();
        assertThat(tomUserDetails.isEnabled()).isTrue();
        assertThat(tomUserDetails.isActivated()).isFalse();
        assertThat(tomUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ACTIVATED"))).isFalse();
        assertThat(tomUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))).isFalse();
    }


}
