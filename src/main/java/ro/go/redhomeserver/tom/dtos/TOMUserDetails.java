package ro.go.redhomeserver.tom.dtos;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.go.redhomeserver.tom.models.Account;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TOMUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorityList;

    public TOMUserDetails(Account account) {
        this.username = account.getUsername();
        this.password = account.getPassword();
        authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(account.getEmployee().getDepartment().getName()));
        if(account.getId() == 1)
            authorityList.add(new SimpleGrantedAuthority("ADMIN"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
