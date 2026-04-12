package io.cred0.core.roles.api;

import java.util.List;
import java.util.UUID;

public record RoleRequestDto(
        String name,
        String description,
        List<UUID> userIds,
        List<UUID> groupIds
) {
}
