package io.cred0.core.roles.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.roles.persistence.RoleEntity;

public interface RoleService {

    List<RoleEntity> findAll();

    RoleEntity findById(UUID id);

    RoleEntity create(RoleEntity role, Set<UUID> userIds, Set<UUID> groupIds);

    RoleEntity update(UUID id, RoleEntity role, Set<UUID> userIds, Set<UUID> groupIds);

    void deleteById(UUID id);
}
