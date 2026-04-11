package io.cred0.core.users;

import java.util.List;
import java.util.UUID;

import io.cred0.core.users.api.UserAttributeDto;
import io.cred0.core.users.api.UserCredentialsRequestDto;
import io.cred0.core.users.api.UserRequestDto;
import io.cred0.core.users.api.UserResponseDto;
import io.cred0.core.users.mappers.UserEntityMapper;
import io.cred0.core.users.persistence.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityMapperTest {

    @Test
    void fromCreateRequestMapsAllFields() {
        UserRequestDto request = new UserRequestDto(
                "  jdoe  ",
                "  John  ",
                "  Doe  ",
                "  john.doe@example.com  ",
                true,
                false,
                List.of(new UserAttributeDto("department", "engineering")),
                new UserCredentialsRequestDto("password", "temporary-secret")
        );

        UserEntity entity = UserEntityMapper.fromCreateRequest(request);

        assertEquals("jdoe", entity.getUsername());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals("john.doe@example.com", entity.getEmail());
        assertTrue(entity.isEnabled());
        assertFalse(entity.isEmailVerified());
        assertNotNull(entity.getAttributes());
        assertEquals("password", entity.getCredentialsType());
        assertEquals("temporary-secret", entity.getCredentialsValue());
    }

    @Test
    void toResponseDtoRedactsCredentialValue() {
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setUsername("jdoe");
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setEmail("john.doe@example.com");
        entity.setCreatedTimestamp(100L);
        entity.setLastModifiedTimestamp(200L);
        entity.setEnabled(true);
        entity.setEmailVerified(false);
        entity.setAttributes("[{\"name\":\"department\",\"value\":\"engineering\"}]");
        entity.setCredentialsType("password");
        entity.setCredentialsValue("secret");

        UserResponseDto response = UserEntityMapper.toResponseDto(entity);

        assertNotNull(response.credentials());
        assertEquals("password", response.credentials().type());
        assertTrue(response.credentials().secretSet());
        assertEquals("department", response.attributes().getFirst().name());
        assertEquals("engineering", response.attributes().getFirst().value());
    }
}
