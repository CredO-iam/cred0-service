package io.cred0.core.users.api;

public record UserCredentialsRequestDto(
        String type,
        String value
) {
}
