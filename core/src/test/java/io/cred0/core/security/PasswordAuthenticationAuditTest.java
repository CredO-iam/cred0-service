package io.cred0.core.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
class PasswordAuthenticationAuditTest {

    private final PasswordAuthenticationAudit listener = new PasswordAuthenticationAudit();

    @Test
    void recordsOnlyFixedOutcomeFields(CapturedOutput output) {
        var token = UsernamePasswordAuthenticationToken.unauthenticated("sample-user", "sample-password");
        listener.onSuccess(new AuthenticationSuccessEvent(token));
        listener.onFailure(new AuthenticationFailureBadCredentialsEvent(token, new BadCredentialsException("sample-error")));

        assertThat(output.getAll()).contains("event=password_authentication outcome=success",
                "event=password_authentication outcome=failure")
                .doesNotContain("sample-user", "sample-password", "sample-error");
    }

    @Test
    void skipsOtherAuthenticationTypes(CapturedOutput output) {
        var token = new TestingAuthenticationToken("sample-user", "sample-password");
        listener.onSuccess(new AuthenticationSuccessEvent(token));
        listener.onFailure(new AuthenticationFailureBadCredentialsEvent(token, new BadCredentialsException("sample-error")));

        assertThat(output.getAll()).doesNotContain("event=password_authentication");
    }
}
