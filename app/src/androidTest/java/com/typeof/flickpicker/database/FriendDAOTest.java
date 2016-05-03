package com.typeof.flickpicker.database;

import android.test.AndroidTestCase;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.User;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */

public class FriendDAOTest extends AndroidTestCase {

    private FriendDAO mFriendDAO;
    private Database mDatabase;
    private UserDAO mUserDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFriendDAO = App.getFriendDAO();
        mUserDAO = App.getUserDAO();
        mDatabase = App.getDatabaseSeed();
        mDatabase.setUpTables();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabase.dropTables();
    }

    /**
     * Tests addFriend
     * Creates two random users, user1 adds user2 as a friend
     * Asserts that user1 has a friend in his friend list
     * @throws Exception
     */

    public void testAddFriend() throws Exception {

        User user1 = new User("pelle", "password");
        User user2 = new User("johan", "password");

        long id1 = mUserDAO.saveUser(user1);
        long id2 = mUserDAO.saveUser(user2);

        Friend f = new Friend(user1.getId(), user2.getId());
        mFriendDAO.addFriend(f);

        List<User> userFriends = mFriendDAO.getFriendsFromUserId(user1.getId());

        assertEquals(1, userFriends.size());

    }

    /**
     * Tests getFriendsFromUserId
     * Asserts that
     * @throws Exception
     */

    public void testGetFriendsFromUserId() throws Exception {
        User user1 = new User("pelle", "password");
        long id = mUserDAO.saveUser(user1);

        List<User> userFriends = mFriendDAO.getFriendsFromUserId(id);
        assertTrue(userFriends != null);
    }

    /**
     * Tests removeFriend
     * Creates two random users
     * Adds user2 to user1s friend list
     * Removes user2 from user1s friend list
     * @throws Exception
     */

    public void testRemoveFriend() throws Exception {

        User user1 = new User("pelle", "password");
        User user2 = new User("johan", "password");

        long id1 = mUserDAO.saveUser(user1);
        long id2 = mUserDAO.saveUser(user2);

        Friend f = new Friend(id1, id2);
        long id = mFriendDAO.addFriend(f);
        mFriendDAO.removeFriend(user1.getId(), user2.getId());
        List<User> userFriends = mFriendDAO.getFriendsFromUserId(user1.getId());

        assertTrue(userFriends.size() == 0);

    }
}
