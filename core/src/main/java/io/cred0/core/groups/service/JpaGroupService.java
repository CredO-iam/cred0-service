package io.cred0.core.groups.service;

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
public class JpaGroupService implements GroupService {

    private final JpaGroupRepository groupRepository;
    private final JpaUserEntityRepository userRepository;
    private final JpaRoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GroupEntity> findAll() {
        return this.groupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public GroupEntity findById(UUID id) {
        return this.groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + id));
    }

    @Override
    @Transactional
    public GroupEntity create(GroupEntity group, Set<UUID> userIds, Set<UUID> roleIds) {
        assertNameIsUnique(group.getName(), null);

        long now = Instant.now().toEpochMilli();
        group.setId(UUID.randomUUID());
        group.setCreatedTimestamp(now);
        group.setLastModifiedTimestamp(now);

        replaceUsers(group, resolveUsers(userIds));
        replaceRoles(group, resolveRoles(roleIds));

        return saveWithConflictMapping(group);
    }

    @Override
    @Transactional
    public GroupEntity update(UUID id, GroupEntity group, Set<UUID> userIds, Set<UUID> roleIds) {
        GroupEntity existing = this.groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + id));

        assertNameIsUnique(group.getName(), id);

        existing.setName(group.getName());
        existing.setDescription(group.getDescription());
        existing.setLastModifiedTimestamp(Instant.now().toEpochMilli());

        replaceUsers(existing, resolveUsers(userIds));
        replaceRoles(existing, resolveRoles(roleIds));

        return saveWithConflictMapping(existing);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        GroupEntity existing = this.groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException("Group not found: " + id));

        for (UserEntity user : new HashSet<>(existing.getUsers())) {
            user.getGroups().remove(existing);
        }
        existing.getUsers().clear();

        for (RoleEntity role : new HashSet<>(existing.getRoles())) {
            role.getGroups().remove(existing);
        }
        existing.getRoles().clear();

        this.groupRepository.delete(existing);
    }

    private void assertNameIsUnique(String name, UUID currentId) {
        this.groupRepository.findByNameIgnoreCase(name)
                .filter(found -> !found.getId().equals(currentId))
                .ifPresent(found -> {
                    throw new GroupConflictException("group name already exists");
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
            throw new GroupNotFoundException("User not found: " + missingId);
        }
        return new HashSet<>(users);
    }

    private Set<RoleEntity> resolveRoles(Set<UUID> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Set.of();
        }
        List<RoleEntity> roles = this.roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            UUID missingId = roleIds.stream().filter(id -> roles.stream().noneMatch(role -> role.getId().equals(id))).findFirst()
                    .orElseThrow();
            throw new GroupNotFoundException("Role not found: " + missingId);
        }
        return new HashSet<>(roles);
    }

    private void replaceUsers(GroupEntity group, Set<UserEntity> users) {
        for (UserEntity user : new HashSet<>(group.getUsers())) {
            user.getGroups().remove(group);
        }
        group.getUsers().clear();
        for (UserEntity user : users) {
            group.getUsers().add(user);
            user.getGroups().add(group);
        }
    }

    private void replaceRoles(GroupEntity group, Set<RoleEntity> roles) {
        for (RoleEntity role : new HashSet<>(group.getRoles())) {
            role.getGroups().remove(group);
        }
        group.getRoles().clear();
        for (RoleEntity role : roles) {
            group.getRoles().add(role);
            role.getGroups().add(group);
        }
    }

    private GroupEntity saveWithConflictMapping(GroupEntity group) {
        try {
            return this.groupRepository.save(group);
        }
        catch (DataIntegrityViolationException ex) {
            throw new GroupConflictException("group name already exists", ex);
        }
    }
}
