package com.fleet.management.exception;

/**
 * Exception thrown when a resource with unique constraints already exists.
 * Maps to HTTP 409 Conflict.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
