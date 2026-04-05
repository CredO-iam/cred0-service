package io.cred0.core.clients.api;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

public record RegisteredClientDto(
        String id,
        String clientId,
        Instant clientIdIssuedAt,
        String clientSecret,
        Instant clientSecretExpiresAt,
        String clientName,
        Set<String> clientAuthenticationMethods,
        Set<String> authorizationGrantTypes,
        Set<String> redirectUris,
        Set<String> postLogoutRedirectUris,
        Set<String> scopes,
        Map<String, Object> clientSettings,
        Map<String, Object> tokenSettings
) {

}
