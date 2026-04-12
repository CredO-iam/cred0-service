package io.cred0.core.bootstrap;

import java.time.Instant;
import java.util.UUID;

import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.groups.persistence.JpaGroupRepository;
import io.cred0.core.roles.persistence.JpaRoleRepository;
import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AdminBootstrapProperties.class)
public class AdminBootstrapInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapInitializer.class);

    static final String DEFAULT_ROLE_NAME = "system_admin";
    static final String DEFAULT_GROUP_NAME = "admins";

    private final AdminBootstrapProperties properties;
    private final JpaUserEntityRepository userRepository;
    private final JpaGroupRepository groupRepository;
    private final JpaRoleRepository roleRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        String adminUsername = resolveUsername();
        log.info("Bootstrap: ensuring admin entities exist (username={})", adminUsername);

        RoleEntity role = ensureRole(DEFAULT_ROLE_NAME);
        GroupEntity group = ensureGroup(DEFAULT_GROUP_NAME);
        UserEntity user = ensureUser(adminUsername);

        ensureGroupHasRole(group, role);
        ensureUserInGroup(group, user);

        log.info("Bootstrap: admin bootstrap complete");
    }

    private String resolveUsername() {
        String username = properties.getUsername();
        if (username == null || username.isBlank()) {
            return "admin";
        }
        return username;
    }

    private RoleEntity ensureRole(String name) {
        return roleRepository.findByNameIgnoreCase(name).orElseGet(() -> {
            log.info("Bootstrap: creating role '{}'", name);
            RoleEntity role = new RoleEntity();
            role.setId(UUID.randomUUID());
            role.setName(name);
            long now = Instant.now().toEpochMilli();
            role.setCreatedTimestamp(now);
            role.setLastModifiedTimestamp(now);
            return roleRepository.save(role);
        });
    }

    private GroupEntity ensureGroup(String name) {
        return groupRepository.findByNameIgnoreCase(name).orElseGet(() -> {
            log.info("Bootstrap: creating group '{}'", name);
            GroupEntity group = new GroupEntity();
            group.setId(UUID.randomUUID());
            group.setName(name);
            long now = Instant.now().toEpochMilli();
            group.setCreatedTimestamp(now);
            group.setLastModifiedTimestamp(now);
            return groupRepository.save(group);
        });
    }

    private UserEntity ensureUser(String username) {
        return userRepository.findByUsernameIgnoreCase(username).orElseGet(() -> {
            log.info("Bootstrap: creating user '{}'", username);
            UserEntity user = new UserEntity();
            user.setId(UUID.randomUUID());
            user.setUsername(username);
            user.setFirstName("Admin");
            user.setLastName("User");
            user.setEmail(resolveEmail());
            user.setEnabled(properties.isEnabled());
            user.setEmailVerified(false);
            user.setAttributes("[]");
            user.setCredentialsType(properties.getCredentialsType());
            user.setCredentialsValue(properties.getPassword());
            long now = Instant.now().toEpochMilli();
            user.setCreatedTimestamp(now);
            user.setLastModifiedTimestamp(now);
            return userRepository.save(user);
        });
    }

    private String resolveEmail() {
        String email = properties.getEmail();
        if (email == null || email.isBlank()) {
            return "admin@localhost";
        }
        return email;
    }

    private void ensureGroupHasRole(GroupEntity group, RoleEntity role) {
        if (!group.getRoles().contains(role)) {
            log.info("Bootstrap: linking group '{}' -> role '{}'", group.getName(), role.getName());
            group.getRoles().add(role);
            role.getGroups().add(group);
            groupRepository.save(group);
        }
    }

    private void ensureUserInGroup(GroupEntity group, UserEntity user) {
        if (!group.getUsers().contains(user)) {
            log.info("Bootstrap: linking user '{}' -> group '{}'", user.getUsername(), group.getName());
            group.getUsers().add(user);
            user.getGroups().add(group);
            groupRepository.save(group);
        }
    }
}
