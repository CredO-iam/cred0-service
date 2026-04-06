package io.cred0.core.users.api;

import java.util.UUID;
import java.util.regex.Pattern;

import io.cred0.core.users.service.UserValidationException;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class UserRequestValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public static UUID parseUuid(String rawId) {
        try {
            return UUID.fromString(rawId);
        }
        catch (Exception ex) {
            throw new UserValidationException("id must be a valid UUID");
        }
    }

    public static void validateForCreate(UserRequestDto request) {
        validateCommon(request);
        validateCredentials(request.credentials(), true);
    }

    public static void validateForUpdate(UserRequestDto request) {
        validateCommon(request);
        validateCredentials(request.credentials(), false);
    }

    private static void validateCommon(UserRequestDto request) {
        if (request == null) {
            throw new UserValidationException("request body is required");
        }

        if (!StringUtils.hasText(request.username())) {
            throw new UserValidationException("username is required");
        }
        if (!StringUtils.hasText(request.firstName())) {
            throw new UserValidationException("firstName is required");
        }
        if (!StringUtils.hasText(request.lastName())) {
            throw new UserValidationException("lastName is required");
        }
        if (!StringUtils.hasText(request.email())) {
            throw new UserValidationException("email is required");
        }
        if (!EMAIL_PATTERN.matcher(request.email().trim()).matches()) {
            throw new UserValidationException("email must be a valid email address");
        }

        if (request.attributes() != null) {
            for (UserAttributeDto attribute : request.attributes()) {
                if (attribute == null || !StringUtils.hasText(attribute.name()) || !StringUtils.hasText(attribute.value())) {
                    throw new UserValidationException("attributes must contain non-empty name and value");
                }
            }
        }
    }

    private static void validateCredentials(UserCredentialsRequestDto credentials, boolean required) {
        if (credentials == null) {
            if (required) {
                throw new UserValidationException("credentials are required");
            }
            return;
        }

        boolean hasType = StringUtils.hasText(credentials.type());
        boolean hasValue = StringUtils.hasText(credentials.value());

        if (!hasType || !hasValue) {
            if (required) {
                throw new UserValidationException("credentials type and value are required");
            }
            throw new UserValidationException("credentials type and value must both be provided");
        }
    }
}
