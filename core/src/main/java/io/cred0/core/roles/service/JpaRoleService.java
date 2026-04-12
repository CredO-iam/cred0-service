package io.cred0.core.roles.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.groups.persistence.GroupEntity;
import io.cred0.core.groups.persistence.JpaGroupRepository;
import io.cred0.core.roles.persistence.JpaRoleRepository;
import io.cred0.core.roles.persistence.RoleEntity;
import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaRoleService implements RoleService {

    private final JpaRoleRepository roleRepository;
    private final JpaUserEntityRepository userRepository;
    private final JpaGroupRepository groupRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoleEntity> findAll() {
        return this.roleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleEntity findById(UUID id) {
        return this.roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + id));
    }

    @Override
    @Transactional
    public RoleEntity create(RoleEntity role, Set<UUID> userIds, Set<UUID> groupIds) {
        assertNameIsUnique(role.getName(), null);

        long now = Instant.now().toEpochMilli();
        role.setId(UUID.randomUUID());
        role.setCreatedTimestamp(now);
        role.setLastModifiedTimestamp(now);

        replaceUsers(role, resolveUsers(userIds));
        replaceGroups(role, resolveGroups(groupIds));

        return saveWithConflictMapping(role);
    }

    @Override
    @Transactional
    public RoleEntity update(UUID id, RoleEntity role, Set<UUID> userIds, Set<UUID> groupIds) {
        RoleEntity existing = this.roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + id));

        assertNameIsUnique(role.getName(), id);

        existing.setName(role.getName());
        existing.setDescription(role.getDescription());
        existing.setLastModifiedTimestamp(Instant.now().toEpochMilli());

        replaceUsers(existing, resolveUsers(userIds));
        replaceGroups(existing, resolveGroups(groupIds));

        return saveWithConflictMapping(existing);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        RoleEntity existing = this.roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found: " + id));

        for (UserEntity user : new HashSet<>(existing.getUsers())) {
            user.getRoles().remove(existing);
        }
        existing.getUsers().clear();

        for (GroupEntity group : new HashSet<>(existing.getGroups())) {
            group.getRoles().remove(existing);
        }
        existing.getGroups().clear();

        this.roleRepository.delete(existing);
    }

    private void assertNameIsUnique(String name, UUID currentId) {
        this.roleRepository.findByNameIgnoreCase(name)
                .filter(found -> !found.getId().equals(currentId))
                .ifPresent(found -> {
                    throw new RoleConflictException("role name already exists");
                });
    }

    private Set<UserEntity> resolveUsers(Set<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Set.of();
        }
        List<UserEntity> users = this.userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            UUID missingId = userIds.stream().filter(id -> users.stream().noneMatch(user -> user.getId().equals(id))).findFirst()
                    .orElseThrow();
            throw new RoleNotFoundException("User not found: " + missingId);
        }
        return new HashSet<>(users);
    }

    private Set<GroupEntity> resolveGroups(Set<UUID> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            return Set.of();
        }
        List<GroupEntity> groups = this.groupRepository.findAllById(groupIds);
        if (groups.size() != groupIds.size()) {
            UUID missingId = groupIds.stream().filter(id -> groups.stream().noneMatch(group -> group.getId().equals(id))).findFirst()
                    .orElseThrow();
            throw new RoleNotFoundException("Group not found: " + missingId);
        }
        return new HashSet<>(groups);
    }

    private void replaceUsers(RoleEntity role, Set<UserEntity> users) {
        for (UserEntity user : new HashSet<>(role.getUsers())) {
            user.getRoles().remove(role);
        }
        role.getUsers().clear();
        for (UserEntity user : users) {
            role.getUsers().add(user);
            user.getRoles().add(role);
        }
    }

    private void replaceGroups(RoleEntity role, Set<GroupEntity> groups) {
        for (GroupEntity group : new HashSet<>(role.getGroups())) {
            group.getRoles().remove(role);
        }
        role.getGroups().clear();
        for (GroupEntity group : groups) {
            role.getGroups().add(group);
            group.getRoles().add(role);
        }
    }

    private RoleEntity saveWithConflictMapping(RoleEntity role) {
        try {
            return this.roleRepository.save(role);
        }
        catch (DataIntegrityViolationException ex) {
            throw new RoleConflictException("role name already exists", ex);
        }
    }
}
