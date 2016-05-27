package com.typeof.flickpicker.database.DAO;

import com.typeof.flickpicker.BaseTest;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.UserDAO;
import com.typeof.flickpicker.database.sql.tables.UserTable;
import java.util.List;

/**
 * UserDAOTest
 *
 * A test class for testing the implementation of the UserDAO interface methods.
 */

public class UserDAOTest extends BaseTest {

    private UserDAO mUserDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mUserDao = App.getUserDAO();
    }


    /**
     * Tests if a record is saved in the database
     * @throws Exception
     */
    public void testSaveUser() throws Exception {

        User user = new User("Username123", "Password123");
        long userID = mUserDao.saveUser(user);

        //assert that the user have been given an ID larger than 0
        //and therefore successfully have been saved in the database
        assertTrue(userID > 0);

    }


    /**
     * Tests if we can find a record in the database
     * @throws Exception
     */
    public void testGetUserById() throws Exception {

        long userId = mUserDao.saveUser(new User("Sibelius", "Password"));
        User foundUser = mUserDao.getUserById(userId);

        //Checks if this indeed is the same user
        assertEquals("checks if username matches", "Sibelius", foundUser.getUsername());

    }

    /**
     * Tests if we can search for a user record in the database
     * @throws Exception
     */
    public void testSearchUser() throws Exception {

        long userId = mUserDao.saveUser(new User("Frodo", "123"));
        mUserDao.getUserById(userId);

        //creates list with search hits with search string "Fro"
        List<User> searchResults = mUserDao.searchUser(UserTable.UserEntry.COLUMN_NAME_USERNAME, "Fro");
        assertEquals(1, searchResults.size());

        //checks to see if the id matches with dummy
        User foundUser = searchResults.get(0);
        assertEquals(userId, foundUser.getId());

    }


    /**
     * Tests if we can change the record of a existing user in the database
     * @throws Exception
     */
    public void testUpdate() throws Exception {


        User user = new User("Luke", "Skywalker");
        long userId = mUserDao.saveUser(user);
        assertTrue(userId > 0);

        //sets score to 10 instead of default value 0 and save user to database
        user.setScore(10);
        mUserDao.saveUser(user);

        //fetch the user again and check score
        User userFetched = mUserDao.getUserById(userId);
        assertEquals(10, userFetched.getScore());
    }



    /**
     * Tests if we can delete a record in the database
     * @throws Exception
     */
    public void testDeleteUser() throws Exception {
        long userId = mUserDao.saveUser(new User("Terminator", "Skynet123"));
        User foundUser = mUserDao.getUserById(userId);

        //delete method returns number of rows affected
        int numberOfUsersDeleted = mUserDao.deleteUser(foundUser);
        assertEquals(1, numberOfUsersDeleted);
    }

    /**
     * Tests method for getting all the users from the database.
     * Creates a couple of users
     * Runs getAllUsers method
     * Asserts the size of added users should be equal to return value of getAllUsers
     *
     * @throws Exception
     */
    public void testGetAllUsers() throws Exception{

        User u1 = new User("user1", "test");
        User u2 = new User("user2", "test");
        User u3 = new User("user3", "test");

        mUserDao.saveUser(u1);
        mUserDao.saveUser(u2);
        mUserDao.saveUser(u3);

        List<User> allUsers = mUserDao.getAllUsers();

        assertEquals(3, allUsers.size());
    }
}