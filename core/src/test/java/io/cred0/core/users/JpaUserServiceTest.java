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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaUserServiceTest {

    @Mock
    private JpaUserEntityRepository repository;

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

        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = service.create(input);

        assertTrue(result.getCreatedTimestamp() > 0);
        assertEquals(result.getCreatedTimestamp(), result.getLastModifiedTimestamp());
        assertTrue(result.getId() != null);
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

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserEntity result = service.update(id, update);

        assertEquals(id, result.getId());
        assertEquals(111L, result.getCreatedTimestamp());
        assertTrue(result.getLastModifiedTimestamp() >= 111L);
    }

    @Test
    void deleteThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.deleteById(id));
    }
}
