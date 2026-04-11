package io.cred0.core.users.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserEntityRepository extends JpaRepository<UserEntity, UUID> {
}
