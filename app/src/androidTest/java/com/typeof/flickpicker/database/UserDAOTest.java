package com.typeof.flickpicker.database;

import android.test.AndroidTestCase;

import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.sql.SQLUserDAO;

import java.security.spec.ECField;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class UserDAOTest extends AndroidTestCase {

    private SQLUserDAO mSQLUserDAO;
    private DatabaseSeed mDatabaseSeed;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSQLUserDAO = new SQLUserDAO(getContext());
        mDatabaseSeed = new DatabaseSeed(getContext());
        mDatabaseSeed.seedDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabaseSeed.clearDatabase();
    }

    public void testSaveUser() throws Exception {
        //creates test user
        User user = new User(123, "Gandalf", "WhiteWiz");

        //saves in database
        long userID = mSQLUserDAO.saveUser(user);

        //checks if return value in userID matches ID used in constructor
        assertEquals(123, userID);


    }

    public void testGetUserById() throws Exception {

        


    }

    public void testDeleteUser() throws Exception {

    }

}