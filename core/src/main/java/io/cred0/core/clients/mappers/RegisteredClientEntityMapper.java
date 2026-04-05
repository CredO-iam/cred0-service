package io.cred0.core.clients.mappers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.cred0.core.clients.config.RegisteredClientRepositoryConfig;
import io.cred0.core.clients.persistence.RegisteredClientEntity;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RegisteredClientEntityMapper {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private static final JsonMapper jsonMapper = RegisteredClientRepositoryConfig.registeredClientJsonMapper();

    public static RegisteredClientEntity toEntity(RegisteredClient client) {
        RegisteredClientEntity entity = new RegisteredClientEntity();
        entity.setId(client.getId());
        entity.setClientId(client.getClientId());
        entity.setClientIdIssuedAt(client.getClientIdIssuedAt() != null ? client.getClientIdIssuedAt() : Instant.now());
        entity.setClientSecret(client.getClientSecret());
        entity.setClientSecretExpiresAt(client.getClientSecretExpiresAt());
        entity.setClientName(client.getClientName());
        entity.setClientAuthenticationMethods(toCommaDelimited(
                client.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).toList()));
        entity.setAuthorizationGrantTypes(toCommaDelimited(
                client.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).toList()));
        entity.setRedirectUris(toCommaDelimited(client.getRedirectUris()));
        entity.setPostLogoutRedirectUris(toCommaDelimited(client.getPostLogoutRedirectUris()));
        entity.setScopes(toCommaDelimited(client.getScopes()));
        entity.setClientSettings(writeMap(client.getClientSettings().getSettings()));
        entity.setTokenSettings(writeMap(client.getTokenSettings().getSettings()));
        return entity;
    }

    public static RegisteredClient toRegisteredClient(RegisteredClientEntity entity) {
        RegisteredClient.Builder builder = RegisteredClient.withId(entity.getId())
                .clientId(entity.getClientId())
                .clientIdIssuedAt(entity.getClientIdIssuedAt())
                .clientSecret(entity.getClientSecret())
                .clientSecretExpiresAt(entity.getClientSecretExpiresAt())
                .clientName(entity.getClientName())
                .clientAuthenticationMethods(methods -> parseCommaDelimited(entity.getClientAuthenticationMethods())
                        .forEach(it -> methods.add(RegisteredClientMapper.toClientAuthenticationMethod(it))))
                .authorizationGrantTypes(types -> parseCommaDelimited(entity.getAuthorizationGrantTypes())
                        .forEach(it -> types.add(RegisteredClientMapper.toAuthorizationGrantType(it))))
                .redirectUris(uris -> uris.addAll(parseCommaDelimited(entity.getRedirectUris())))
                .postLogoutRedirectUris(uris -> uris.addAll(parseCommaDelimited(entity.getPostLogoutRedirectUris())))
                .scopes(scopes -> scopes.addAll(parseCommaDelimited(entity.getScopes())));

        Map<String, Object> clientSettingsMap = readMap(entity.getClientSettings());
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = readMap(entity.getTokenSettings());
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.withSettings(tokenSettingsMap);
        if (!tokenSettingsMap.containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
            tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        }
        builder.tokenSettings(tokenSettingsBuilder.build());

        return builder.build();
    }

    private static Map<String, Object> readMap(String data) {
        if (!StringUtils.hasText(data)) {
            return new HashMap<>();
        }
        try {
            return jsonMapper.readValue(data, MAP_TYPE);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private static String writeMap(Map<String, Object> data) {
        try {
            return jsonMapper.writeValueAsString(data);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private static String toCommaDelimited(Set<String> values) {
        return toCommaDelimited(new ArrayList<>(values));
    }

    private static String toCommaDelimited(List<String> values) {
        return values.stream().filter(StringUtils::hasText).collect(Collectors.joining(","));
    }

    private static List<String> parseCommaDelimited(String value) {
        if (!StringUtils.hasText(value)) {
            return List.of();
        }
        return Arrays.stream(value.split(",")).filter(StringUtils::hasText).toList();
    }

}

