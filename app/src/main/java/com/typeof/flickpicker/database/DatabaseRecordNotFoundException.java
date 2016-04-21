package com.typeof.flickpicker.database;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-21.
 */
public class DatabaseRecordNotFoundException extends RuntimeException {
    public DatabaseRecordNotFoundException(String message) {
        super(message);
    }
}
