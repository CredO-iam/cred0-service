package io.cred0.core.bootstrap;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cred0.bootstrap.admin")
public class AdminBootstrapProperties {

    private String username = "admin";
    private String password = "change_me_admin_password";
    private String email = "admin@localhost";
    private String credentialsType = "password";
    private boolean enabled = true;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCredentialsType() {
        return credentialsType;
    }

    public void setCredentialsType(String credentialsType) {
        this.credentialsType = credentialsType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
