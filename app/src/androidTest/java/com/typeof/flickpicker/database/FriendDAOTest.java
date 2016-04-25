package com.typeof.flickpicker.database;

import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.sql.SQLFriendDAO;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */

public class FriendDAOTest extends AndroidTestCase {

    private FriendDAO fFriendDAO;
    private DatabaseSeed fDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fFriendDAO = new SQLFriendDAO(getContext());
        fDatabaseSeed = new DatabaseSeed(getContext());
        fDatabaseSeed.seedDatabase();
    }
}
