package com.typeof.flickpicker.database;

import android.test.ApplicationTestCase;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Friend;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;
import com.typeof.flickpicker.core.User;

import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */

public class FriendDAOTest extends ApplicationTestCase<App> {

    private FriendDAO mFriendDAO;
    private UserDAO mUserDAO;
    private RatingDAO mRatingDAO;
    private MovieDAO mMovieDAO;

    public FriendDAOTest() {
        super(App.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();

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
     * Tests addFriend
     * Creates two random users, user1 adds user2 as a friend
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
        mFriendDAO.addFriend(f);
        mFriendDAO.removeFriend(user1.getId(), user2.getId());
        List<User> userFriends = mFriendDAO.getFriendsFromUserId(user1.getId());

        assertTrue(userFriends.size() == 0);

    }

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

    public void testUpdateFriendMatches(){

        //create three users
        long primaryUser = mUserDAO.saveUser(new User("Pelle", "admin"));
        long secondaryUser = mUserDAO.saveUser(new User("Kalle", "admin"));
        long thirdUser = mUserDAO.saveUser(new User("Olle", "admin"));

        //add kalle && olle as a friends to pelle
        long firstFriendshipId = mFriendDAO.addFriend(new Friend(primaryUser,secondaryUser));
        long secondFriendshipId = mFriendDAO.addFriend(new Friend(primaryUser,thirdUser));

        //let them rate the same two movies
        long americanHistoryXId = mMovieDAO.saveMovie(new Movie("American History X", 2000));
        long planetOfTheApesId = mMovieDAO.saveMovie(new Movie("Planet of the apes", 1998));

        long firstRatingPelle = mRatingDAO.saveRating(new Rating(2,americanHistoryXId,primaryUser));
        long secondRatingPelle = mRatingDAO.saveRating(new Rating(3, planetOfTheApesId, primaryUser));
        long firstRatingKalle = mRatingDAO.saveRating(new Rating(4, americanHistoryXId,secondaryUser));
        long secondRatingKalle = mRatingDAO.saveRating(new Rating(5, planetOfTheApesId, secondaryUser));
        long firstRatingOlle = mRatingDAO.saveRating(new Rating(2,americanHistoryXId,thirdUser));
        long secondRatingOlle = mRatingDAO.saveRating(new Rating(3, planetOfTheApesId,thirdUser));

        Rating pellesRatingOnAmericanHistoryX = mRatingDAO.findRating(firstRatingPelle);

        mFriendDAO.updateFriendMatches(pellesRatingOnAmericanHistoryX);

        //fetch the relationship and compare the dismatch value to the expected one:
        //if implemented correctly - should return 2; (# diffs [abs(3-5)+abs(3-5)]/#nmbrOfMoviesBothSeen = (2+2)/2) && 0.0

        Friend friendRelationOne = mFriendDAO.getFriendRelation(primaryUser,secondaryUser);
        assertEquals(2.0,friendRelationOne.getDisMatch());
        Friend friendRelationTwo = mFriendDAO.getFriendRelation(primaryUser,thirdUser);
        assertEquals(0.0, friendRelationTwo.getDisMatch());
    }

    public void testGetFriendRelation(){

        long primaryUser = mUserDAO.saveUser(new User("Pelle", "admin"));
        long secondaryUser = mUserDAO.saveUser(new User("Kalle", "admin"));

        Friend friendShip = new Friend(primaryUser,secondaryUser);
        long friendshipId = mFriendDAO.addFriend(friendShip);

        //test and confirm that friendshipId && the fetchedFriendshipId are the same
        Friend fetchedFriendship = mFriendDAO.getFriendRelation(primaryUser,secondaryUser);
        long fetchedFriendshipId = fetchedFriendship.getId();
        assertEquals(friendshipId,fetchedFriendshipId);
    }
}
