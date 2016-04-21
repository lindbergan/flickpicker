package com.typeof.flickpicker.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import junit.framework.TestCase;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class UserDatabaseHelperTest extends AndroidTestCase {

    private UserDatabaseHelper mUserDatabaseHelper;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mUserDatabaseHelper = new UserDatabaseHelper(getContext());
        mDatabaseSeed = new DatabaseSeed(getContext());
        mDatabaseSeed.seedDatabase();
    }
}