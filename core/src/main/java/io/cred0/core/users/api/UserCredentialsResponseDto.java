package io.cred0.core.users.api;

public record UserCredentialsResponseDto(
        String type,
        boolean secretSet
) {
}
