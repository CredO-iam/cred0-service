package io.cred0.core.clients.persistence;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "oauth2_registered_client",
        uniqueConstraints = @UniqueConstraint(name = "uk_oauth2_registered_client_client_id", columnNames = "client_id")
)
@Getter
@Setter
public class RegisteredClientEntity {

    @Id
    @Column(name = "id", nullable = false, length = 100)
    private String id;

    @Column(name = "client_id", nullable = false, length = 200)
    private String clientId;

    @Column(name = "client_id_issued_at")
    private Instant clientIdIssuedAt;

    @Column(name = "client_secret", length = 500)
    private String clientSecret;

    @Column(name = "client_secret_expires_at")
    private Instant clientSecretExpiresAt;

    @Column(name = "client_name", nullable = false, length = 200)
    private String clientName;

    @Column(name = "client_authentication_methods", nullable = false, length = 1000)
    private String clientAuthenticationMethods;

    @Column(name = "authorization_grant_types", nullable = false, length = 1000)
    private String authorizationGrantTypes;

    @Column(name = "redirect_uris", length = 1000)
    private String redirectUris;

    @Column(name = "post_logout_redirect_uris", length = 1000)
    private String postLogoutRedirectUris;

    @Column(name = "scopes", nullable = false, length = 1000)
    private String scopes;

    @Lob
    @Column(name = "client_settings", nullable = false)
    private String clientSettings;

    @Lob
    @Column(name = "token_settings", nullable = false)
    private String tokenSettings;


}

