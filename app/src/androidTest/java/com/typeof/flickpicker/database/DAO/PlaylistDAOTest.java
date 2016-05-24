package com.typeof.flickpicker.database.DAO;

import com.typeof.flickpicker.BaseTest;
import com.typeof.flickpicker.application.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.MovieDAO;
import com.typeof.flickpicker.database.PlaylistDAO;
import junit.framework.Assert;
import java.util.ArrayList;
import java.util.List;

/**
 * PlaylistDAOTest
 *
 * A test class for testing the implementation of the PlaylistDAO interface methods.
 */

public class PlaylistDAOTest extends BaseTest {

    private PlaylistDAO mPlaylistDAO;
    private MovieDAO mMovieDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mPlaylistDAO = App.getPlaylistDAO();
        mMovieDAO = App.getMovieDAO();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests getUserPlaylist()
     *
     * Creates a user and saves it
     * Creates three movies and saves them
     * Creates a list of Numbers and save the movisIds to the list
     * Creates and saves a new playlist, giving it a title, userId and the newly created list of movieIds
     * Asserts that the call to the playlists method getMovieIds().size() equals three
     */

    public void testGetUserPlaylist() {

        User currentUser = new User("CurrentUser", "admin");
        long currentUserId = App.getUserDAO().saveUser(currentUser);

        Movie m1 = new Movie("Movie1", 1995);
        Movie m2 = new Movie("Movie2", 1996);
        Movie m3 = new Movie("Movie3", 1997);
        mMovieDAO.saveMovie(m1);
        mMovieDAO.saveMovie(m2);
        mMovieDAO.saveMovie(m3);

        List<Number> movieIds = new ArrayList<>();
        movieIds.add(m1.getId());
        movieIds.add(m2.getId());
        movieIds.add(m3.getId());


        Playlist playlist = new Playlist("Watchlist", currentUserId, movieIds);
        mPlaylistDAO.savePlaylist(playlist);

        Playlist resultPlaylist = mPlaylistDAO.getUserPlaylist(currentUserId);
        assertTrue(resultPlaylist.getMovieIds().size() == 3);
    }

    /**
     * Tests findPlaylistById()
     *
     * Creates a playlist and saves its id
     * Fetches a playlist by calling findPlaylistById() with the newly created playlist's id as a parameter
     * Asserts that the newly created playlist's id and the fetched playlist's id are the same
     */

    public void testFindPlaylistById() {
        Playlist playlist = new Playlist("My favourites", 5, new ArrayList<Number>(){{add(20); add(30); add(40);}});
        long id = mPlaylistDAO.savePlaylist(playlist);
        Playlist foundPlaylist = mPlaylistDAO.findPlaylistById(id);
        assertEquals(id, foundPlaylist.getId());
    }

    /**
     * Tests savePlaylist()
     *
     * Creates a playlist and saves it's id
     * Asserts that the saved id isn't 0
     */

    public void testSavePlaylist() {
        Playlist playlist = new Playlist("My favourites", 5);
        long id = mPlaylistDAO.savePlaylist(playlist);
        assertTrue(id != 0);
    }

    /**
     * Tests removePlaylist()
     *
     * Creates a playlist and saves it's id
     * Calls removePlaylist()
     * Asserts that when calling findPlaylistById() with the recently deleted playlist,
     * the method should return an exception that can be handled
     */

    public void testRemovePlaylist() {
        Playlist playlist = new Playlist("My favorites", 5);
        long id = mPlaylistDAO.savePlaylist(playlist);
        mPlaylistDAO.removePlaylist(playlist);

        try {
            mPlaylistDAO.findPlaylistById(id);
            Assert.fail();
        }
        catch (DatabaseRecordNotFoundException e) {
            assertTrue("Playlist removed!", true);
        }
    }
}