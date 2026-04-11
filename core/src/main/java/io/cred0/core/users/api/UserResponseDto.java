package io.cred0.core.users.api;

import java.util.List;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String firstName,
        Long createdTimestamp,
        Long lastModifiedTimestamp,
        String lastName,
        String email,
        boolean enabled,
        boolean emailVerified,
        List<UserAttributeDto> attributes,
        UserCredentialsResponseDto credentials
) {
}
