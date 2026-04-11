package io.cred0.core.roles;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.groups.persistence.JpaGroupRepository;
import io.cred0.core.roles.persistence.JpaRoleRepository;
import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.roles.service.JpaRoleService;
import io.cred0.core.roles.service.RoleConflictException;
import io.cred0.core.roles.service.RoleNotFoundException;
import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
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
class JpaRoleServiceTest {

    @Mock
    private JpaRoleRepository roleRepository;

    @Mock
    private JpaUserEntityRepository userRepository;

    @Mock
    private JpaGroupRepository groupRepository;

    @InjectMocks
    private JpaRoleService service;

    @Test
    void createSetsTimestampsAndRelations() {
        UUID userId = UUID.randomUUID();
        UUID groupId = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(userId);
        GroupEntity group = new GroupEntity();
        group.setId(groupId);

        RoleEntity input = new RoleEntity();
        input.setName("manager");

        when(roleRepository.findByNameIgnoreCase("manager")).thenReturn(Optional.empty());
        when(userRepository.findAllById(Set.of(userId))).thenReturn(java.util.List.of(user));
        when(groupRepository.findAllById(Set.of(groupId))).thenReturn(java.util.List.of(group));
        when(roleRepository.save(any(RoleEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RoleEntity result = service.create(input, Set.of(userId), Set.of(groupId));

        assertTrue(result.getCreatedTimestamp() > 0);
        assertEquals(result.getCreatedTimestamp(), result.getLastModifiedTimestamp());
        assertEquals(1, result.getUsers().size());
        assertEquals(1, result.getGroups().size());
        assertTrue(user.getRoles().contains(result));
        assertTrue(group.getRoles().contains(result));
        verify(roleRepository).save(result);
    }

    @Test
    void createThrowsConflictWhenNameExists() {
        RoleEntity existing = new RoleEntity();
        existing.setId(UUID.randomUUID());
        when(roleRepository.findByNameIgnoreCase("manager")).thenReturn(Optional.of(existing));

        RoleEntity input = new RoleEntity();
        input.setName("manager");

        assertThrows(RoleConflictException.class, () -> service.create(input, Set.of(), Set.of()));
    }

    @Test
    void createThrowsNotFoundWhenLinkedGroupMissing() {
        UUID missingGroupId = UUID.randomUUID();
        RoleEntity input = new RoleEntity();
        input.setName("manager");

        when(roleRepository.findByNameIgnoreCase("manager")).thenReturn(Optional.empty());
        when(groupRepository.findAllById(Set.of(missingGroupId))).thenReturn(java.util.List.of());

        assertThrows(RoleNotFoundException.class, () -> service.create(input, Set.of(), Set.of(missingGroupId)));
    }
}
