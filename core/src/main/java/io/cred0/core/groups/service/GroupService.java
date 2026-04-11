package io.cred0.core.groups.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.cred0.core.groups.persistence.GroupEntity;

public interface GroupService {

    List<GroupEntity> findAll();

    GroupEntity findById(UUID id);

    GroupEntity create(GroupEntity group, Set<UUID> userIds, Set<UUID> roleIds);

    GroupEntity update(UUID id, GroupEntity group, Set<UUID> userIds, Set<UUID> roleIds);

    void deleteById(UUID id);
}
