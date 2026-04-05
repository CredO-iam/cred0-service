package io.cred0.core.clients.service;

import java.util.List;

import io.cred0.core.clients.mappers.RegisteredClientEntityMapper;
import io.cred0.core.clients.persistence.JpaRegisteredClientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class JpaRegisteredClientRepository implements RegisteredClientRepository {

    private final JpaRegisteredClientEntityRepository repository;

    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        Assert.notNull(registeredClient, "registeredClient cannot be null");
        this.repository.save(RegisteredClientEntityMapper.toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        Assert.hasText(id, "id cannot be empty");
        return this.repository.findById(id).map(RegisteredClientEntityMapper::toRegisteredClient).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        Assert.hasText(clientId, "clientId cannot be empty");
        return this.repository.findByClientId(clientId).map(RegisteredClientEntityMapper::toRegisteredClient).orElse(null);
    }

    public List<RegisteredClient> findAll() {
        return this.repository.findAll().stream().map(RegisteredClientEntityMapper::toRegisteredClient).toList();
    }

    @Transactional
    public void deleteById(String id) {
        this.repository.deleteById(id);
    }


}
