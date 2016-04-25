package com.typeof.flickpicker.database;

import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.sql.SQLUserDAO;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class UserSQLDAOTest extends AndroidTestCase {

    private SQLUserDAO mSQLUserDAO;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSQLUserDAO = new SQLUserDAO(getContext());
        mDatabaseSeed = new DatabaseSeed(getContext());
        mDatabaseSeed.seedDatabase();
    }
}