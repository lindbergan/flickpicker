package com.typeof.flickpicker.database;

/**
 * Exception thrown when the Database Record is not found
 */
public class DatabaseRecordNotFoundException extends RuntimeException {
    public DatabaseRecordNotFoundException(String message) {
        super(message);
    }
}
