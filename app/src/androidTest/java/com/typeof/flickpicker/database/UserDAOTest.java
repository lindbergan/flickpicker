package com.typeof.flickpicker.database;

import android.test.AndroidTestCase;

import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.sql.SQLUserDAO;
import com.typeof.flickpicker.database.sql.UserTable;

import java.security.spec.ECField;
import java.util.List;

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

    /**
     * Tests if a record is saved in the database
     * @throws Exception
     */
    public void testSaveUser() throws Exception {

        //creates test user
        User user = new User("Gandalf", "WhiteWiz");

        //saves in database
        long userID = mSQLUserDAO.saveUser(user);

        //checks if return value in userID matches ID used in constructor
        assertTrue(userID > 0);

    }

    /**
     * Tests if we can find a record in the database
     * @throws Exception
     */
    public void testGetUserById() throws Exception {

        //return dummy user that was added to database via mDatabaseSeed
        User user = mSQLUserDAO.getUserById(12);

        //Checks if this indeed is the same user
        assertEquals("checks if usernames matches", "Frodo", user.getUsername());

    }

    /**
     * Tests if we can search for a user record in the database
     * @throws Exception
     */
    public void testSearchUser() throws Exception {

        //creates list with search hits with search string "Fro"
        List<User> results = mSQLUserDAO.searchUser(UserTable.UserEntry.COLUMN_NAME_USERNAME, "Fro");

        //checks if we indeed get one result, the dummy user in the database
        assertEquals(1, results.size());

        //checks to see if the id matches with dummy
        User foundUser = results.get(0);
        assertEquals(12, foundUser.getId());

    }

    /**
     * Tests if we can change the record of a existing user in the database
     * @throws Exception
     */
    public void testUpdate() throws Exception {

        //creates new user to test update method on
        //checks if it was saved in database correctly
        User user = new User("Luke", "Skywalker");
        long userId = mSQLUserDAO.saveUser(user);

        assertFalse(userId == -1 || userId == 0);

        //sets score to 10 instead of default value (0)
        //call for saveUser which should call for update in SQLDAO
        user.setScore(10);
        mSQLUserDAO.saveUser(user);

        //fetch the user again
        //checks if the score indeed have been updated
        User userFetched = mSQLUserDAO.getUserById(userId);

        assertEquals(10, userFetched.getScore());
    }


    /**
     * Tests if we can delete a record in the database
     * @throws Exception
     */
    public void testDeleteUser() throws Exception {

        //return dummy user that was added to database via mDatabaseSeed
        User user = mSQLUserDAO.getUserById(12);

        //delete method returns number of rows affected
        int numberOfUsersDeleted = mSQLUserDAO.deleteUser(user);

        //checks that only one user was deleted
        assertEquals(1, numberOfUsersDeleted);
    }
}