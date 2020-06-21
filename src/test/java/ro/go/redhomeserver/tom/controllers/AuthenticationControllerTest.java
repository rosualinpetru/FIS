package ro.go.redhomeserver.tom.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ro.go.redhomeserver.tom.configuration.CustomAccessDeniedHandler;
import ro.go.redhomeserver.tom.configuration.CustomAuthenticationSuccessHandler;
import ro.go.redhomeserver.tom.configuration.SecurityConfiguration;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.Department;
import ro.go.redhomeserver.tom.models.Employee;
import ro.go.redhomeserver.tom.services.AuthenticationService;
import ro.go.redhomeserver.tom.services.ClearDataService;
import ro.go.redhomeserver.tom.services.TOMUserDetailService;

import java.util.Date;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
@Import({SecurityConfiguration.class, CustomAccessDeniedHandler.class, CustomAuthenticationSuccessHandler.class})
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClearDataService clearDataService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private TOMUserDetailService tomUserDetailService;

    @Test
    void getSaltShouldReturnValueOfAuthenticationServiceResponse() throws Exception {
        when(authenticationService.getSaltOfUser("user")).thenReturn("salt");
        mockMvc.perform(get("/get-salt?username=user"))
                .andExpect(status().isOk())
                .andExpect(content().string("salt"));
    }

    @Test
    void logInShouldDisplayLogInPageIfAnonymous() throws Exception {
        mockMvc.perform(get("/log-in"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void logInShouldRedirectMeToIndexForAuthenticated() throws Exception {
        mockMvc.perform(get("/log-in"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser(authorities = {"ACTIVATED"})
    void indexShouldRedirectToLogOutIfUserNotFoundException() {
        try {
            when(authenticationService.getMyEmployeeData(anyString())).then(invocation -> {
                throw new UserNotFoundException("Failed");
            });
            mockMvc.perform(get("/"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/tom/log-out"));
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }

    @Test
    @WithMockUser(authorities = {"ACTIVATED"})
    void indexShouldForwardToIndexIfUserIsFoundAndActivated() {
        try {
            Employee employee = new Employee("Name", "Address", "Phone", 5000, "Email", new Date(), new Department("IT"));
            employee.setAccount(new Account());
            when(authenticationService.getMyEmployeeData(anyString())).thenReturn(employee);
            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("isTeamLeader", false))
                    .andExpect(model().attribute("employee", employee));
        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }
}
