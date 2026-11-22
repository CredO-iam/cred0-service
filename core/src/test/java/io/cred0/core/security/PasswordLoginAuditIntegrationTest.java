package io.cred0.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(properties = {
        "cred0.bootstrap.admin.username=audit-test-admin",
        "cred0.bootstrap.admin.password=test-login-password",
        "spring.datasource.url=jdbc:h2:mem:audit-login"
})
@ExtendWith(OutputCaptureExtension.class)
class PasswordLoginAuditIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AuthenticationEventPublisher events;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void recordsSuccessfulLogin(CapturedOutput output) throws Exception {
        mvc.perform(formLogin().user("audit-test-admin").password("test-login-password"))
                .andExpect(authenticated());
        assertSingleOutcome(output, "success");
    }

    @Test
    void recordsFailedLogin(CapturedOutput output) throws Exception {
        mvc.perform(formLogin().user("audit-test-admin").password("wrong-test-password"))
                .andExpect(unauthenticated());
        assertSingleOutcome(output, "failure");
    }

    @Test
    void recordsUserStoreFailure(CapturedOutput output) {
        events.publishAuthenticationFailure(new InternalAuthenticationServiceException("user store unavailable"),
                UsernamePasswordAuthenticationToken.unauthenticated("sample-user", "sample-password"));

        assertSingleOutcome(output, "failure");
    }

    private void assertSingleOutcome(CapturedOutput output, String outcome) {
        String prefix = "event=password_authentication";
        assertThat(output.getAll().lines()
                .filter(line -> line.contains(prefix))
                .map(line -> line.substring(line.indexOf(prefix))))
                .containsExactly(prefix + " outcome=" + outcome);
    }
}
