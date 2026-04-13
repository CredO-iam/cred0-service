package io.cred0.core.users;

import java.util.Optional;
import java.util.UUID;

import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
import io.cred0.core.users.service.JpaUserService;
import io.cred0.core.users.service.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaUserServiceTest {

    @Mock
    private JpaUserEntityRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JpaUserService service;

    @Test
    void createSetsTimestampsAndGeneratedId() {
        UserEntity input = new UserEntity();
        input.setUsername("jdoe");
        input.setFirstName("John");
        input.setLastName("Doe");
        input.setEmail("john.doe@example.com");
        input.setAttributes("[]");
        input.setCredentialsType("password");
        input.setCredentialsValue("temporary-secret");

        when(passwordEncoder.encode("temporary-secret")).thenReturn("ENC(temporary-secret)");
        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = service.create(input);

        assertTrue(result.getCreatedTimestamp() > 0);
        assertEquals(result.getCreatedTimestamp(), result.getLastModifiedTimestamp());
        assertNotNull(result.getId());
        assertEquals("ENC(temporary-secret)", result.getCredentialsValue());
        verify(repository).save(result);
    }

    @Test
    void updatePreservesCreatedTimestampAndRefreshesLastModifiedTimestamp() {
        UUID id = UUID.randomUUID();
        UserEntity existing = new UserEntity();
        existing.setId(id);
        existing.setCreatedTimestamp(111L);

        UserEntity update = new UserEntity();
        update.setUsername("jdoe");
        update.setFirstName("Jane");
        update.setLastName("Doe");
        update.setEmail("jane.doe@example.com");
        update.setAttributes("[]");
        update.setCredentialsType("password");
        update.setCredentialsValue("new-secret");

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(passwordEncoder.encode("new-secret")).thenReturn("ENC(new-secret)");
        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = service.update(id, update);

        assertEquals(id, result.getId());
        assertEquals(111L, result.getCreatedTimestamp());
        assertTrue(result.getLastModifiedTimestamp() >= 111L);
        assertEquals("ENC(new-secret)", result.getCredentialsValue());
    }

    @Test
    void deleteThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.deleteById(id));
    }
}
