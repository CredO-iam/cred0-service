package io.cred0.core.users.service;

public class UserConflictException extends RuntimeException {

    public UserConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
