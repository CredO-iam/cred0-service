package io.cred0.core.users.api;

import java.util.List;

public record UserRequestDto(
        String username,
        String firstName,
        String lastName,
        String email,
        Boolean enabled,
        Boolean emailVerified,
        List<UserAttributeDto> attributes,
        UserCredentialsRequestDto credentials
) {
}
