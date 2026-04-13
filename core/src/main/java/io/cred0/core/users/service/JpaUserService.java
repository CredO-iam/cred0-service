package io.cred0.core.users.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import io.cred0.core.users.persistence.JpaUserEntityRepository;
import io.cred0.core.users.persistence.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class JpaUserService implements UserService {

    private final JpaUserEntityRepository repository;
    private final PasswordEncoder passwordEncoder;

    public static final String USER_NOT_FOUND = "User not found: ";

    @Override
    public List<UserEntity> findAll() {
        return this.repository.findAll();
    }

    @Override
    public UserEntity findById(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + id));
    }

    @Override
    @Transactional
    public UserEntity create(UserEntity user) {
        encodePasswordCredentialsIfNeeded(user);
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
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + id));

        encodePasswordCredentialsIfNeeded(user);
        user.setId(existing.getId());
        user.setCreatedTimestamp(existing.getCreatedTimestamp());
        user.setLastModifiedTimestamp(Instant.now().toEpochMilli());

        return saveWithConflictMapping(user);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        UserEntity existing = this.repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + id));

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

    private void encodePasswordCredentialsIfNeeded(UserEntity user) {
        if (!"password".equalsIgnoreCase(user.getCredentialsType())) {
            return;
        }

        if (!StringUtils.hasText(user.getCredentialsValue())) {
            return;
        }

        String rawOrEncoded = user.getCredentialsValue().trim();

        user.setCredentialsValue(passwordEncoder.encode(rawOrEncoded));
    }

}
