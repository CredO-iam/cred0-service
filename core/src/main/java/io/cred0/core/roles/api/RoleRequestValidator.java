package io.cred0.core.roles.api;

import java.util.List;
import java.util.UUID;

import io.cred0.core.roles.service.RoleValidationException;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class RoleRequestValidator {

    public static UUID parseUuid(String rawId) {
        try {
            return UUID.fromString(rawId);
        }
        catch (Exception ex) {
            throw new RoleValidationException("id must be a valid UUID");
        }
    }

    public static void validateForCreate(RoleRequestDto request) {
        validateCommon(request);
    }

    public static void validateForUpdate(RoleRequestDto request) {
        validateCommon(request);
    }

    private static void validateCommon(RoleRequestDto request) {
        if (request == null) {
            throw new RoleValidationException("request body is required");
        }
        if (!StringUtils.hasText(request.name())) {
            throw new RoleValidationException("name is required");
        }
        validateIds(request.userIds(), "userIds");
        validateIds(request.groupIds(), "groupIds");
    }

    private static void validateIds(List<UUID> ids, String fieldName) {
        if (ids == null) {
            return;
        }
        if (ids.stream().anyMatch(id -> id == null)) {
            throw new RoleValidationException(fieldName + " must contain valid UUID values");
        }
    }
}
