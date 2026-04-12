package io.cred0.core.users.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JpaUserService implements UserService {

    private final JpaUserEntityRepository repository;

    @Override
    public List<UserEntity> findAll() {
        return this.repository.findAll();
    }

    @Override
    public UserEntity findById(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    @Override
    @Transactional
    public UserEntity create(UserEntity user) {
        long now = Instant.now().toEpochMilli();
        user.setId(UUID.randomUUID());
        user.setCreatedTimestamp(now);
        user.setLastModifiedTimestamp(now);
        return saveWithConflictMapping(user);
    }

    @Override
    @Transactional
    public UserEntity update(UUID id, UserEntity user) {
        UserEntity existing = this.repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));

        user.setId(existing.getId());
        user.setCreatedTimestamp(existing.getCreatedTimestamp());
        user.setLastModifiedTimestamp(Instant.now().toEpochMilli());

        return saveWithConflictMapping(user);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        UserEntity existing = this.repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));

        existing.getGroups().forEach(group -> group.getUsers().remove(existing));
        existing.getRoles().forEach(role -> role.getUsers().remove(existing));
        existing.setGroups(new HashSet<>());
        existing.setRoles(new HashSet<>());
        this.repository.delete(existing);
    }

    private UserEntity saveWithConflictMapping(UserEntity user) {
        try {
            return this.repository.save(user);
        }
        catch (DataIntegrityViolationException ex) {
            throw new UserConflictException("username or email already exists", ex);
        }
    }
}
