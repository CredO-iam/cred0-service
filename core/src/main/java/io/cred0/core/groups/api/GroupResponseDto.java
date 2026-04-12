package io.cred0.core.groups.api;

import java.util.List;
import java.util.UUID;

public record GroupResponseDto(
        UUID id,
        String name,
        String description,
        Long createdTimestamp,
        Long lastModifiedTimestamp,
        List<UUID> userIds,
        List<UUID> roleIds
) {
}
