package io.cred0.core.groups.api;

import java.util.List;
import java.util.UUID;

public record GroupRequestDto(
        String name,
        String description,
        List<UUID> userIds,
        List<UUID> roleIds
) {
}
