package io.cred0.core.clients.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class RegisteredClientMapper {

    static AuthorizationGrantType toAuthorizationGrantType(String value) {
        return switch (value) {
            case "authorization_code" -> AuthorizationGrantType.AUTHORIZATION_CODE;
            case "client_credentials" -> AuthorizationGrantType.CLIENT_CREDENTIALS;
            case "refresh_token" -> AuthorizationGrantType.REFRESH_TOKEN;
            default -> new AuthorizationGrantType(value);
        };
    }

    static ClientAuthenticationMethod toClientAuthenticationMethod(String value) {
        return switch (value) {
            case "client_secret_basic" -> ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
            case "client_secret_post" -> ClientAuthenticationMethod.CLIENT_SECRET_POST;
            case "none" -> ClientAuthenticationMethod.NONE;
            default -> new ClientAuthenticationMethod(value);
        };
    }

    static Set<String> toAuthorizationGrantTypeValues(Set<AuthorizationGrantType> grantTypes) {
        return grantTypes.stream().map(AuthorizationGrantType::getValue).collect(Collectors.toSet());
    }

    static Set<String> toClientAuthenticationMethodValues(Set<ClientAuthenticationMethod> methods) {
        return methods.stream().map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet());
    }

}

