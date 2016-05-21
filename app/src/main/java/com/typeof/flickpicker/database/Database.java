package com.typeof.flickpicker.database;

import android.content.Context;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-03.
 */
public interface Database {
    void setUpTables();
    void dropTables();
    void seedDatabase();
}
