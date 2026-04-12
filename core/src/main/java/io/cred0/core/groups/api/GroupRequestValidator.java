package io.cred0.core.groups.api;

import java.util.List;
import java.util.UUID;

import io.cred0.core.groups.service.GroupValidationException;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class GroupRequestValidator {

    public static UUID parseUuid(String rawId) {
        try {
            return UUID.fromString(rawId);
        }
        catch (Exception ex) {
            throw new GroupValidationException("id must be a valid UUID");
        }
    }

    public static void validateForCreate(GroupRequestDto request) {
        validateCommon(request);
    }

    public static void validateForUpdate(GroupRequestDto request) {
        validateCommon(request);
    }

    private static void validateCommon(GroupRequestDto request) {
        if (request == null) {
            throw new GroupValidationException("request body is required");
        }
        if (!StringUtils.hasText(request.name())) {
            throw new GroupValidationException("name is required");
        }
        validateIds(request.userIds(), "userIds");
        validateIds(request.roleIds(), "roleIds");
    }

    private static void validateIds(List<UUID> ids, String fieldName) {
        if (ids == null) {
            return;
        }
        if (ids.stream().anyMatch(id -> id == null)) {
            throw new GroupValidationException(fieldName + " must contain valid UUID values");
        }
    }
}
