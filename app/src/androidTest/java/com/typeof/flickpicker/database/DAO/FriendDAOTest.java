package com.typeof.flickpicker.database.DAO;

import com.typeof.flickpicker.BaseTest;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.FriendDAO;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.RatingDAO;
import com.typeof.flickpicker.database.UserDAO;

import java.util.List;

/**
 * FriendDAOTest
 *
 * A test class for testing the implementation of the FriendDAO interface methods.
 */

public class FriendDAOTest extends BaseTest {

    private FriendDAO mFriendDAO;
    private UserDAO mUserDAO;
    private RatingDAO mRatingDAO;
    private MovieDAO mMovieDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mFriendDAO = App.getFriendDAO();
        mUserDAO = App.getUserDAO();
        mMovieDAO = App.getMovieDAO();
        mRatingDAO = App.getRatingDAO();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests addFriend()
     *
     * Creates two random users where user1 adds user2 as a friend
     * Asserts that user1 has a friend in his friend list
     * @throws Exception
     */
    public void testAddFriend() throws Exception {

        User user1 = new User("pelle", "password");
        User user2 = new User("johan", "password");

        mUserDAO.saveUser(user1);
        mUserDAO.saveUser(user2);

        Friend f = new Friend(user1.getId(), user2.getId());
        mFriendDAO.addFriend(f);

        List<User> userFriends = mFriendDAO.getFriendsFromUserId(user1.getId());

        assertEquals(1, userFriends.size());

    }

    /**
     * Tests getFriendsFromUserId()
     *
     * Creates two users and saves them
     * Adds the second user as a friend to primary user and saves that friend
     * Calls getFriendsFromUserId() and saves the results
     * Asserts that the list of friends has the expected size of 1
     * @throws Exception
     */
    public void testGetFriendsFromUserId() throws Exception {

        long primaryUserId = mUserDAO.saveUser(new User("pelle", "password"));
        long secondaryUserId = mUserDAO.saveUser(new User("kalle", "password"));

        Friend friend = new Friend(primaryUserId,secondaryUserId);
        mFriendDAO.addFriend(friend);

        List<User> primaryUsersFriends = mFriendDAO.getFriendsFromUserId(primaryUserId);
        assertEquals(1,primaryUsersFriends.size());
    }

    /**
     * Tests removeFriend()
     *
     * Creates two random users
     * Adds user2 to user1s friend list
     * Removes user2 from user1s friend list
     * Asserts that the list of friends returns an empty list of users
     * @throws Exception
     */
    public void testRemoveFriend() throws Exception {

        User user1 = new User("pelle", "password");
        User user2 = new User("johan", "password");

        long id1 = mUserDAO.saveUser(user1);
        long id2 = mUserDAO.saveUser(user2);

        Friend f = new Friend(id1, id2);
        mFriendDAO.addFriend(f);
        mFriendDAO.removeFriend(id1,id2);
        List<User> userFriends = mFriendDAO.getFriendsFromUserId(user1.getId());

        assertTrue(userFriends.size() == 0);

    }

    /**
     * Tests getFriendLatestActivities()
     *
     * Creates four random users
     * Adds three of them as friends to the first user
     * Create three ratings where two of them is by the first users friends
     * Asserts that the list of ratings has the expected size of two
     */

    public void testGetFriendsLatestActivities() {

        Movie terminator = new Movie("Terminator 2: Judgement Day", 1992);
        mMovieDAO.saveMovie(terminator);

        User sebbe = new User("sebastian", "123");
        User adde = new User("adrian", "321");
        User jolo = new User("jolo", "321");
        User sunken = new User("sunken", "321");
        long userId1 = mUserDAO.saveUser(sebbe);
        long userId2 = mUserDAO.saveUser(adde);
        long userId3 = mUserDAO.saveUser(jolo);
        long userId4 = mUserDAO.saveUser(sunken);

        mFriendDAO.addFriend(new Friend(userId1, userId2));
        mFriendDAO.addFriend(new Friend(userId1, userId3));
        mFriendDAO.addFriend(new Friend(userId1, userId4));

        Rating r1 = new Rating(5, terminator.getId(),userId1);
        Rating r2 = new Rating(3, terminator.getId(),userId2);
        Rating r3 = new Rating(5, terminator.getId(),userId3);

        mRatingDAO.saveRating(r1);
        mRatingDAO.saveRating(r2);
        mRatingDAO.saveRating(r3);

        List<Rating> friendsRating = mFriendDAO.getFriendsLatestActivities(userId1);

        assertEquals(2, friendsRating.size());

    }

    /**
     * Tests updateFriendMatches()
     *
     * Creates three random users
     * Adds two of them as friends to primary user
     * All three of them rates the same two movies
     * Primary users ratings should be compared to the friends' ratings of the same movies
     * and translated into a mismatch value if the method works as it's supposed to.
     * Asserts that the mismatch value corresponds to the expected value for each user
     */

    public void testUpdateFriendMatches(){

        long primaryUser = mUserDAO.saveUser(new User("Pelle", "admin"));
        long secondaryUser = mUserDAO.saveUser(new User("Kalle", "admin"));
        long thirdUser = mUserDAO.saveUser(new User("Olle", "admin"));

        mFriendDAO.addFriend(new Friend(primaryUser,secondaryUser));
        mFriendDAO.addFriend(new Friend(primaryUser,thirdUser));

        long americanHistoryXId = mMovieDAO.saveMovie(new Movie("American History X", 2000));
        long planetOfTheApesId = mMovieDAO.saveMovie(new Movie("Planet of the Apes", 1998));

        long firstRatingPelle = mRatingDAO.saveRating(new Rating(3,americanHistoryXId,primaryUser));
        long secondRatingPelle = mRatingDAO.saveRating(new Rating(3, planetOfTheApesId, primaryUser));
        long firstRatingKalle = mRatingDAO.saveRating(new Rating(5, americanHistoryXId,secondaryUser));
        long secondRatingKalle = mRatingDAO.saveRating(new Rating(5, planetOfTheApesId, secondaryUser));
        long firstRatingOlle = mRatingDAO.saveRating(new Rating(3,americanHistoryXId,thirdUser));
        long secondRatingOlle = mRatingDAO.saveRating(new Rating(3, planetOfTheApesId,thirdUser));

        Rating pellesRatingOnAmericanHistoryX = mRatingDAO.findRating(firstRatingPelle);

        mFriendDAO.updateFriendMatches(pellesRatingOnAmericanHistoryX);

        // fetch the relationship and compare the mismatch value to the expected one - if implemented correctly:
        // Should return: 2.0 [abs(3-5)+abs(3-5)]/#nmbrOfMoviesBothSeen = (2+2)/2
        // && 0.0 [abs(3-3)+abs(3-3)]/#nmbrOfMoviesBothSeen = (0+0)/2

        Friend friendRelationOne = mFriendDAO.getFriendRelation(primaryUser,secondaryUser);
        assertEquals(2.0,friendRelationOne.getMismatch());
        Friend friendRelationTwo = mFriendDAO.getFriendRelation(primaryUser,thirdUser);
        assertEquals(0.0, friendRelationTwo.getMismatch());
    }

    /**
     * Tests getFriendRelation()
     *
     * Creates two random users
     * Adds one of them as friend to primary user and saves the friend object
     * Fetches the friend object and confirms that the fetched and created friend object's userIdTwo are the same
     * Asserts that the userIdTwo of the two objects are the same
     */

    public void testGetFriendRelation() {

        long primaryUser = mUserDAO.saveUser(new User("Pelle", "admin"));
        long secondaryUser = mUserDAO.saveUser(new User("Kalle", "admin"));

        Friend friendship = new Friend(primaryUser, secondaryUser);
        long friendshipId = mFriendDAO.addFriend(friendship);

        Friend fetchedFriendship = mFriendDAO.getFriendRelation(primaryUser, secondaryUser);
        assertEquals(friendship.getGetUserIdTwo(), fetchedFriendship.getGetUserIdTwo());
    }

    /**
     * Tests isFriend()
     *
     * Creates a random users
     * Adds that user as a friend to main user
     * Asserts that the relation has been added to the database
     */

    public void testIsFriend() {
        User u = new User("testFriend", "testPassword");
        mUserDAO.saveUser(u);
        mFriendDAO.addFriend(new Friend(App.getCurrentUser().getId(), u.getId()));
        assertTrue(mFriendDAO.isFriend(u.getId()));
    }
}
