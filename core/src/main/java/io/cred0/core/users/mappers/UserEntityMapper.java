package io.cred0.core.users.mappers;

import java.util.ArrayList;
import java.util.List;

import io.cred0.core.users.api.UserAttributeDto;
import io.cred0.core.users.api.UserCredentialsRequestDto;
import io.cred0.core.users.api.UserCredentialsResponseDto;
import io.cred0.core.users.api.UserRequestDto;
import io.cred0.core.users.api.UserResponseDto;
import io.cred0.core.users.persistence.UserEntity;
import io.cred0.core.users.service.UserValidationException;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class UserEntityMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<UserAttributeDto>> ATTRIBUTES_TYPE = new TypeReference<>() {
    };

    public static UserEntity fromCreateRequest(UserRequestDto request) {
        UserEntity entity = new UserEntity();
        mapCommonFields(request, entity);
        mapCredentials(request.credentials(), entity);
        return entity;
    }

    public static UserEntity fromUpdateRequest(UserRequestDto request) {
        UserEntity entity = new UserEntity();
        mapCommonFields(request, entity);
        mapCredentials(request.credentials(), entity);
        return entity;
    }

    public static UserResponseDto toResponseDto(UserEntity entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getUsername(),
                entity.getFirstName(),
                entity.getCreatedTimestamp(),
                entity.getLastModifiedTimestamp(),
                entity.getLastName(),
                entity.getEmail(),
                entity.isEnabled(),
                entity.isEmailVerified(),
                readAttributes(entity.getAttributes()),
                toCredentialsResponse(entity)
        );
    }

    public static List<UserResponseDto> toResponseDtos(List<UserEntity> entities) {
        return entities.stream().map(UserEntityMapper::toResponseDto).toList();
    }

    private static UserCredentialsResponseDto toCredentialsResponse(UserEntity entity) {
        String type = entity.getCredentialsType();
        if (!StringUtils.hasText(type)) {
            return null;
        }
        return new UserCredentialsResponseDto(type, StringUtils.hasText(entity.getCredentialsValue()));
    }

    private static void mapCommonFields(UserRequestDto request, UserEntity entity) {
        entity.setUsername(request.username().trim());
        entity.setFirstName(request.firstName().trim());
        entity.setLastName(request.lastName().trim());
        entity.setEmail(request.email().trim());
        entity.setEnabled(Boolean.TRUE.equals(request.enabled()));
        entity.setEmailVerified(Boolean.TRUE.equals(request.emailVerified()));
        entity.setAttributes(writeAttributes(request.attributes()));
    }

    private static void mapCredentials(UserCredentialsRequestDto credentials, UserEntity entity) {
        if (credentials == null) {
            entity.setCredentialsType(null);
            entity.setCredentialsValue(null);
            return;
        }
        entity.setCredentialsType(credentials.type().trim());
        entity.setCredentialsValue(credentials.value().trim());
    }

    private static String writeAttributes(List<UserAttributeDto> attributes) {
        List<UserAttributeDto> safeAttributes = attributes == null ? List.of() : new ArrayList<>(attributes);
        try {
            return OBJECT_MAPPER.writeValueAsString(safeAttributes);
        }
        catch (Exception ex) {
            throw new UserValidationException("failed to serialize attributes");
        }
    }

    private static List<UserAttributeDto> readAttributes(String rawAttributes) {
        if (!StringUtils.hasText(rawAttributes)) {
            return List.of();
        }

        try {
            List<UserAttributeDto> attributes = OBJECT_MAPPER.readValue(rawAttributes, ATTRIBUTES_TYPE);
            if (attributes == null) {
                return List.of();
            }

            return attributes.stream()
                    .filter(attribute -> attribute != null)
                    .map(attribute -> new UserAttributeDto(
                            attribute.name(),
                            attribute.value()
                    ))
                    .toList();
        }
        catch (Exception ex) {
            throw new UserValidationException("failed to deserialize attributes");
        }
    }

}
