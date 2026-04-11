package io.cred0.core.users.service;

import java.util.List;
import java.util.UUID;

import io.cred0.core.users.persistence.UserEntity;

public interface UserService {

    List<UserEntity> findAll();

    UserEntity findById(UUID id);

    UserEntity create(UserEntity user);

    UserEntity update(UUID id, UserEntity user);

    void deleteById(UUID id);
}
