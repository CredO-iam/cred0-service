package io.cred0.core.groups;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.groups.persistence.JpaGroupRepository;
import io.cred0.core.groups.service.GroupConflictException;
import io.cred0.core.groups.service.GroupNotFoundException;
import io.cred0.core.groups.service.JpaGroupService;
import io.cred0.core.roles.persistence.JpaRoleRepository;
import io.cred0.core.roles.persistence.RoleEntity;
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
class JpaGroupServiceTest {

    @Mock
    private JpaGroupRepository groupRepository;

    @Mock
    private JpaUserEntityRepository userRepository;

    @Mock
    private JpaRoleRepository roleRepository;

    @InjectMocks
    private JpaGroupService service;

    @Test
    void createSetsTimestampsAndRelations() {
        UUID userId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        UserEntity user = new UserEntity();
        user.setId(userId);
        RoleEntity role = new RoleEntity();
        role.setId(roleId);

        GroupEntity input = new GroupEntity();
        input.setName("admins");

        when(groupRepository.findByNameIgnoreCase("admins")).thenReturn(Optional.empty());
        when(userRepository.findAllById(Set.of(userId))).thenReturn(java.util.List.of(user));
        when(roleRepository.findAllById(Set.of(roleId))).thenReturn(java.util.List.of(role));
        when(groupRepository.save(any(GroupEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GroupEntity result = service.create(input, Set.of(userId), Set.of(roleId));

        assertTrue(result.getCreatedTimestamp() > 0);
        assertEquals(result.getCreatedTimestamp(), result.getLastModifiedTimestamp());
        assertEquals(1, result.getUsers().size());
        assertEquals(1, result.getRoles().size());
        assertTrue(user.getGroups().contains(result));
        assertTrue(role.getGroups().contains(result));
        verify(groupRepository).save(result);
    }

    @Test
    void createThrowsConflictWhenNameExists() {
        GroupEntity existing = new GroupEntity();
        existing.setId(UUID.randomUUID());
        when(groupRepository.findByNameIgnoreCase("admins")).thenReturn(Optional.of(existing));

        GroupEntity input = new GroupEntity();
        input.setName("admins");

        assertThrows(GroupConflictException.class, () -> service.create(input, Set.of(), Set.of()));
    }

    @Test
    void createThrowsNotFoundWhenLinkedRoleMissing() {
        UUID missingRoleId = UUID.randomUUID();
        GroupEntity input = new GroupEntity();
        input.setName("admins");

        when(groupRepository.findByNameIgnoreCase("admins")).thenReturn(Optional.empty());
        when(roleRepository.findAllById(Set.of(missingRoleId))).thenReturn(java.util.List.of());

        assertThrows(GroupNotFoundException.class, () -> service.create(input, Set.of(), Set.of(missingRoleId)));
    }
}
