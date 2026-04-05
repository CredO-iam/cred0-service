package io.cred0.core.clients.mappers;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.clients.api.RegisteredClientDto;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RegisteredClientDtoMapper {
    public static RegisteredClientDto toDto(RegisteredClient client) {
        return new RegisteredClientDto(
                client.getId(),
                client.getClientId(),
                client.getClientIdIssuedAt(),
                client.getClientSecret(),
                client.getClientSecretExpiresAt(),
                client.getClientName(),
                RegisteredClientMapper.toClientAuthenticationMethodValues(client.getClientAuthenticationMethods()),
                RegisteredClientMapper.toAuthorizationGrantTypeValues(client.getAuthorizationGrantTypes()),
                client.getRedirectUris(),
                client.getPostLogoutRedirectUris(),
                client.getScopes(),
                client.getClientSettings().getSettings(),
                client.getTokenSettings().getSettings()
        );
    }

    public static RegisteredClient toRegisteredClient(RegisteredClientDto dto) {
        String clientIdToUse = dto.clientId();
        String clientNameToUse = dto.clientName();
        if (!StringUtils.hasText(clientIdToUse)) {
            throw new IllegalArgumentException("clientId cannot be empty");
        }
        if (!StringUtils.hasText(clientNameToUse)) {
            throw new IllegalArgumentException("clientName cannot be empty");
        }

        RegisteredClient.Builder builder = RegisteredClient.withId(
                        StringUtils.hasText(dto.id()) ? dto.id() : UUID.randomUUID().toString())
                .clientId(clientIdToUse)
                .clientName(clientNameToUse)
                .clientIdIssuedAt(dto.clientIdIssuedAt() != null ? dto.clientIdIssuedAt() : Instant.now());

        if (StringUtils.hasText(dto.clientSecret())) {
            builder.clientSecret(dto.clientSecret());
        }
        if (dto.clientSecretExpiresAt() != null) {
            builder.clientSecretExpiresAt(dto.clientSecretExpiresAt());
        }

        Set<String> methods = (dto.clientAuthenticationMethods() == null || dto.clientAuthenticationMethods().isEmpty())
                ? Set.of(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()) : dto.clientAuthenticationMethods();
        methods.forEach(method -> builder.clientAuthenticationMethod(RegisteredClientMapper.toClientAuthenticationMethod(method)));

        Set<String> grantTypes = (dto.authorizationGrantTypes() == null || dto.authorizationGrantTypes().isEmpty())
                ? Set.of(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()) : dto.authorizationGrantTypes();
        grantTypes.forEach(grantType -> builder.authorizationGrantType(RegisteredClientMapper.toAuthorizationGrantType(grantType)));

        if (dto.redirectUris() != null) {
            dto.redirectUris().forEach(builder::redirectUri);
        }
        if (dto.postLogoutRedirectUris() != null) {
            dto.postLogoutRedirectUris().forEach(builder::postLogoutRedirectUri);
        }
        if (dto.scopes() != null) {
            dto.scopes().forEach(builder::scope);
        }

        builder.clientSettings(toClientSettings(dto));
        builder.tokenSettings(toTokenSettings(dto));

        return builder.build();
    }

    private static @NonNull TokenSettings toTokenSettings(RegisteredClientDto dto) {
        return (dto.tokenSettings() == null || dto.tokenSettings().isEmpty()) ? TokenSettings.builder().build() : TokenSettings.withSettings(dto.tokenSettings()).build();
    }

    private static @NonNull ClientSettings toClientSettings(RegisteredClientDto dto) {
        return (dto.clientSettings() == null || dto.clientSettings().isEmpty()) ? ClientSettings.builder().build() : ClientSettings.withSettings(dto.clientSettings()).build();
    }

}

