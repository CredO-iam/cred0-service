package io.cred0.core.security;

import java.util.Optional;
import java.util.UUID;

import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoreUserDetailsServiceTest {

    @Mock
    private JpaUserEntityRepository userRepository;

    @InjectMocks
    private CoreUserDetailsService service;

    @Test
    void loadsUserAndResolvesAuthoritiesFromDirectAndGroupRoles() {
        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setUsername("admin");
        user.setEnabled(true);
        user.setCredentialsType("password");
        user.setCredentialsValue("$2a$10$abcdefghijklmnopqrstuv012345678901234567890123456789");

        RoleEntity directRole = new RoleEntity();
        directRole.setName("ops");
        user.getRoles().add(directRole);

        RoleEntity groupRole = new RoleEntity();
        groupRole.setName("system_admin");
        GroupEntity group = new GroupEntity();
        group.setName("admins");
        group.getRoles().add(groupRole);
        user.getGroups().add(group);

        when(userRepository.findOneByUsernameIgnoreCase("admin")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("admin");

        assertEquals("admin", details.getUsername());
        assertEquals(user.getCredentialsValue(), details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> "ROLE_OPS".equals(a.getAuthority())));
        assertTrue(details.getAuthorities().stream().anyMatch(a -> "ROLE_SYSTEM_ADMIN".equals(a.getAuthority())));
    }

    @Test
    void rejectsUserWithoutPasswordCredentials() {
        UserEntity user = new UserEntity();
        user.setUsername("admin");
        user.setCredentialsType("token");
        user.setCredentialsValue("secret");

        when(userRepository.findOneByUsernameIgnoreCase("admin")).thenReturn(Optional.of(user));

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("admin"));
    }
}

