package io.cred0.core.clients.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRegisteredClientEntityRepository extends JpaRepository<RegisteredClientEntity, String> {

    Optional<RegisteredClientEntity> findByClientId(String clientId);


}

