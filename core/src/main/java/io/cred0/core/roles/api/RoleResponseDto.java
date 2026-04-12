package io.cred0.core.roles.api;

import java.util.List;
import java.util.UUID;

public record RoleResponseDto(
        UUID id,
        String name,
        String description,
        Long createdTimestamp,
        Long lastModifiedTimestamp,
        List<UUID> userIds,
        List<UUID> groupIds
) {
}
