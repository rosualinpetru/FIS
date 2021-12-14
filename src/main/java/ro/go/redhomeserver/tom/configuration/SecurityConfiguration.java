package ro.go.redhomeserver.tom.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import ro.go.redhomeserver.tom.services.TOMUserDetailService;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TOMUserDetailService userDetailsService;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AccessDeniedHandler accessDeniedHandler;

    @Autowired
    public SecurityConfiguration(TOMUserDetailService userDetailsService, AuthenticationSuccessHandler authenticationSuccessHandler, AccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/images/**", "/webjars/**", "/js/**", "/css/**").permitAll()
                .antMatchers("/log-in", "/get-salt", "/reset-password", "/validate-password-reset-request", "/set-new-password", "/healthcheck").permitAll()
                .antMatchers("/create-account", "/sign-up", "/pending-holiday-requests-hr", "/company-schedule").access("hasAuthority('ACTIVATED') and (hasAuthority('HR') or hasAuthority('ADMIN'))")
                .antMatchers("/pending-issues", "/change-team-leader", "/delete-issue").access("hasAuthority('ACTIVATED') and (hasAuthority('IT') or hasAuthority('ADMIN'))")
                .antMatchers("/manage-department", "/add-department", "/delete-department", "/delete-employee").access("hasAuthority('ACTIVATED') and (hasAuthority('IT') and hasAuthority('ADMIN'))")
                .anyRequest().hasAuthority("ACTIVATED")
                .and()
                .formLogin()
                .loginPage("/log-in")
                .loginProcessingUrl("/log-in")
                .successHandler(authenticationSuccessHandler)
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                .logout()
                .logoutUrl("/log-out")
                .deleteCookies("JSESSIONID");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
