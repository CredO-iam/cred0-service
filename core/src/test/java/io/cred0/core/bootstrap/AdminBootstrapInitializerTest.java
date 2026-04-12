package io.cred0.core.bootstrap;

import java.util.Optional;

import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.groups.persistence.JpaGroupRepository;
import io.cred0.core.roles.persistence.JpaRoleRepository;
import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.ApplicationArguments;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminBootstrapInitializerTest {

    @Mock
    private AdminBootstrapProperties properties;

    @Mock
    private JpaUserEntityRepository userRepository;

    @Mock
    private JpaGroupRepository groupRepository;

    @Mock
    private JpaRoleRepository roleRepository;

    @Mock
    private ApplicationArguments applicationArguments;

    @InjectMocks
    private AdminBootstrapInitializer initializer;

    @BeforeEach
    void setupDefaults() {
        when(properties.getUsername()).thenReturn("admin");
        when(properties.getPassword()).thenReturn("change_me_admin_password");
        when(properties.getEmail()).thenReturn("admin@localhost");
        when(properties.getCredentialsType()).thenReturn("password");
        when(properties.isEnabled()).thenReturn(true);
    }

    @Test
    void createsAllEntitiesWhenDatabaseIsEmpty() throws Exception {
        when(roleRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_ROLE_NAME))
                .thenReturn(Optional.empty());
        when(groupRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_GROUP_NAME))
                .thenReturn(Optional.empty());
        when(userRepository.findByUsernameIgnoreCase("admin"))
                .thenReturn(Optional.empty());

        when(roleRepository.save(any(RoleEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(groupRepository.save(any(GroupEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        initializer.run(applicationArguments);

        verify(roleRepository).save(argThat(r -> AdminBootstrapInitializer.DEFAULT_ROLE_NAME.equals(r.getName())));
        verify(groupRepository, times(3)).save(argThat(g -> AdminBootstrapInitializer.DEFAULT_GROUP_NAME.equals(g.getName())));
        verify(userRepository).save(argThat(u -> "admin".equals(u.getUsername())
                && "Admin".equals(u.getFirstName())
                && "User".equals(u.getLastName())
                && "[]".equals(u.getAttributes())
                && !u.isEmailVerified()
                && u.isEnabled()));
    }

    @Test
    void doesNotCreateEntitiesWhenAlreadyPresent() throws Exception {
        RoleEntity existingRole = roleWithName(AdminBootstrapInitializer.DEFAULT_ROLE_NAME);
        GroupEntity existingGroup = groupWithName(AdminBootstrapInitializer.DEFAULT_GROUP_NAME);
        UserEntity existingUser = userWithUsername("admin");

        existingGroup.getRoles().add(existingRole);
        existingGroup.getUsers().add(existingUser);

        when(roleRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_ROLE_NAME))
                .thenReturn(Optional.of(existingRole));
        when(groupRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_GROUP_NAME))
                .thenReturn(Optional.of(existingGroup));
        when(userRepository.findByUsernameIgnoreCase("admin"))
                .thenReturn(Optional.of(existingUser));

        initializer.run(applicationArguments);

        verify(roleRepository, never()).save(any());
        verify(groupRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void reconcilesMissingGroupRoleLinkWhenEntitiesExist() throws Exception {
        RoleEntity existingRole = roleWithName(AdminBootstrapInitializer.DEFAULT_ROLE_NAME);
        GroupEntity existingGroup = groupWithName(AdminBootstrapInitializer.DEFAULT_GROUP_NAME);
        UserEntity existingUser = userWithUsername("admin");

        // Group exists but role link is missing; user link is present
        existingGroup.getUsers().add(existingUser);

        when(roleRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_ROLE_NAME))
                .thenReturn(Optional.of(existingRole));
        when(groupRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_GROUP_NAME))
                .thenReturn(Optional.of(existingGroup));
        when(userRepository.findByUsernameIgnoreCase("admin"))
                .thenReturn(Optional.of(existingUser));

        when(groupRepository.save(any(GroupEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        initializer.run(applicationArguments);

        verify(groupRepository).save(argThat(g -> g.getRoles().contains(existingRole)));
        assertTrue(existingRole.getGroups().contains(existingGroup));
    }

    @Test
    void reconcilesMissingUserGroupLinkWhenEntitiesExist() throws Exception {
        RoleEntity existingRole = roleWithName(AdminBootstrapInitializer.DEFAULT_ROLE_NAME);
        GroupEntity existingGroup = groupWithName(AdminBootstrapInitializer.DEFAULT_GROUP_NAME);
        UserEntity existingUser = userWithUsername("admin");

        // Role link present but user link is missing
        existingGroup.getRoles().add(existingRole);

        when(roleRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_ROLE_NAME))
                .thenReturn(Optional.of(existingRole));
        when(groupRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_GROUP_NAME))
                .thenReturn(Optional.of(existingGroup));
        when(userRepository.findByUsernameIgnoreCase("admin"))
                .thenReturn(Optional.of(existingUser));

        when(groupRepository.save(any(GroupEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        initializer.run(applicationArguments);

        verify(groupRepository).save(argThat(g -> g.getUsers().contains(existingUser)));
        assertTrue(existingUser.getGroups().contains(existingGroup));
    }

    @Test
    void fallsBackToDefaultUsernameWhenBlankProvided() throws Exception {
        when(properties.getUsername()).thenReturn("  ");

        when(roleRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(roleWithName(AdminBootstrapInitializer.DEFAULT_ROLE_NAME)));
        when(groupRepository.findByNameIgnoreCase(any())).thenAnswer(inv -> {
            GroupEntity g = groupWithName(AdminBootstrapInitializer.DEFAULT_GROUP_NAME);
            return Optional.of(g);
        });
        when(userRepository.findByUsernameIgnoreCase("admin")).thenReturn(Optional.of(userWithUsername("admin")));

        GroupEntity capturedGroup = groupWithName(AdminBootstrapInitializer.DEFAULT_GROUP_NAME);
        when(groupRepository.findByNameIgnoreCase(AdminBootstrapInitializer.DEFAULT_GROUP_NAME))
                .thenReturn(Optional.of(capturedGroup));

        UserEntity capturedUser = userWithUsername("admin");
        capturedGroup.getUsers().add(capturedUser);
        capturedGroup.getRoles().add(roleWithName(AdminBootstrapInitializer.DEFAULT_ROLE_NAME));
        when(userRepository.findByUsernameIgnoreCase("admin")).thenReturn(Optional.of(capturedUser));

        initializer.run(applicationArguments);

        verify(userRepository, never()).save(any());
    }

    private RoleEntity roleWithName(String name) {
        RoleEntity role = new RoleEntity();
        role.setId(java.util.UUID.randomUUID());
        role.setName(name);
        role.setCreatedTimestamp(System.currentTimeMillis());
        role.setLastModifiedTimestamp(System.currentTimeMillis());
        return role;
    }

    private GroupEntity groupWithName(String name) {
        GroupEntity group = new GroupEntity();
        group.setId(java.util.UUID.randomUUID());
        group.setName(name);
        group.setCreatedTimestamp(System.currentTimeMillis());
        group.setLastModifiedTimestamp(System.currentTimeMillis());
        return group;
    }

    private UserEntity userWithUsername(String username) {
        UserEntity user = new UserEntity();
        user.setId(java.util.UUID.randomUUID());
        user.setUsername(username);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setEmail("admin@localhost");
        user.setAttributes("[]");
        user.setEnabled(true);
        user.setEmailVerified(false);
        user.setCreatedTimestamp(System.currentTimeMillis());
        user.setLastModifiedTimestamp(System.currentTimeMillis());
        return user;
    }
}
