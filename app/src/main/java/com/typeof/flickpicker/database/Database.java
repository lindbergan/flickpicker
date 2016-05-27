package com.typeof.flickpicker.database;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-03.
 */
public interface Database {
    void setUpTables();
    void dropTables();
    void seedDatabase();
    boolean hasBeenCreated();
}
