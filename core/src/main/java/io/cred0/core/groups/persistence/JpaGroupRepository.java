package io.cred0.core.groups.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaGroupRepository extends JpaRepository<GroupEntity, UUID> {

    Optional<GroupEntity> findByNameIgnoreCase(String name);
}
