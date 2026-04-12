package io.cred0.core.groups.service;

public class GroupConflictException extends RuntimeException {

    public GroupConflictException(String message) {
        super(message);
    }

    public GroupConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
