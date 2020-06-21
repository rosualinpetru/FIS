package ro.go.redhomeserver.tom.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ro.go.redhomeserver.tom.configuration.CustomAccessDeniedHandler;
import ro.go.redhomeserver.tom.configuration.CustomAuthenticationSuccessHandler;
import ro.go.redhomeserver.tom.configuration.SecurityConfiguration;
import ro.go.redhomeserver.tom.exceptions.SystemException;
import ro.go.redhomeserver.tom.services.AccountService;
import ro.go.redhomeserver.tom.services.TOMUserDetailService;

import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
@Import({SecurityConfiguration.class, CustomAccessDeniedHandler.class, CustomAuthenticationSuccessHandler.class})
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TOMUserDetailService tomUserDetailService;

    @Test
    @WithMockUser(authorities = {"HR", "ACTIVATED"})
    void createAccountShouldRedirectToIndexIfHrAndActivatedAndShouldAddUpperNotification() throws Exception {
        mockMvc.perform(get("/create-account"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlTemplate("/tom/"))
                .andExpect(flash().attribute("upperNotification", "The employee record was added and the account was generated!"));
    }

    @Test
    @WithMockUser(authorities = {"HR"})
    void createAccountShouldRedirectToLogOutIfNotActivated() throws Exception {
        mockMvc.perform(get("/create-account"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tom/log-out"));
    }

    @Test
    @WithMockUser(authorities = {"ACTIVATED"})
    void createAccountShouldRedirectToLogOutIfNotHROrAdmin() throws Exception {
        mockMvc.perform(get("/create-account"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tom/log-out"));
    }

    @Test
    @WithMockUser(authorities = {"HR", "ACTIVATED"})
    void createAccountShouldInformItAboutErrorAndUpperNotificationContainsErrorMessage() {
        try {
            doThrow(new SystemException("Error")).when(accountService).generateAccount(anyString(), anyString());
            mockMvc.perform(get("/create-account"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/tom/"))
                    .andExpect(flash().attribute("upperNotification", "There was an error in the system!"));
            verify(accountService, times(1)).informItAboutSystemError("Error");

        } catch (Exception e) {
            fail("Exception interfered!");
        }
    }


}
