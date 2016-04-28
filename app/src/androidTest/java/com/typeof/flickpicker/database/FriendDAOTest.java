package com.typeof.flickpicker.database;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.sql.FriendTable;
import com.typeof.flickpicker.database.sql.PlaylistTable;
import com.typeof.flickpicker.database.sql.SQLFriendDAO;
import com.typeof.flickpicker.database.sql.SQLPlaylistDAO;
import com.typeof.flickpicker.database.sql.SQLUserDAO;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */

public class FriendDAOTest extends AndroidTestCase {

    private FriendDAO fFriendDAO;
    private DatabaseSeed fDatabaseSeed;
    private UserDAO mUserDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fFriendDAO = new SQLFriendDAO(getContext());
        mUserDAO = new SQLUserDAO(getContext());

        fDatabaseSeed = new DatabaseSeed(getContext());
        fDatabaseSeed.seedDatabase();

        SQLiteDatabaseHelper databaseHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());
        db.execSQL(FriendTable.FriendEntry.getSQLCreateTableQuery());

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        fDatabaseSeed.clearDatabase();
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
        fFriendDAO.addFriend(f);

        List<User> userFriends = fFriendDAO.getFriendsFromUserId(user1.getId());

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

        List<User> userFriends = fFriendDAO.getFriendsFromUserId(id);
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
        long id = fFriendDAO.addFriend(f);
        fFriendDAO.removeFriend(user1.getId(), user2.getId());
        List<User> userFriends = fFriendDAO.getFriendsFromUserId(user1.getId());

        assertTrue(userFriends.size() == 0);

    }
}
