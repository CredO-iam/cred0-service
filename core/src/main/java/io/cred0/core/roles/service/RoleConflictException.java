package io.cred0.core.roles.service;

public class RoleConflictException extends RuntimeException {

    public RoleConflictException(String message) {
        super(message);
    }

    public RoleConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
