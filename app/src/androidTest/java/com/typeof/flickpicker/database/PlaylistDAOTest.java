package com.typeof.flickpicker.database;
import android.test.ApplicationTestCase;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.User;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-26.
 */
public class PlaylistDAOTest extends ApplicationTestCase<App> {

    private PlaylistDAO mPlaylistDAO;
    private MovieDAO mMovieDAO;

    public PlaylistDAOTest() {
        super(App.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();

        mPlaylistDAO = App.getPlaylistDAO();
        mMovieDAO = App.getMovieDAO();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

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

        Playlist resultPlaylist = mPlaylistDAO.getPlaylist(currentUserId);
        assertTrue(resultPlaylist.getMovieIds().size() == 3);
    }

    public void testFindPlaylistById() {
        Playlist playlist = new Playlist("My favourites", 5, new ArrayList<Number>(){{add(20); add(30); add(40);}});
        long id = mPlaylistDAO.savePlaylist(playlist);
        Playlist foundPlaylist = mPlaylistDAO.findPlaylistById(id);
        assertEquals(id, foundPlaylist.getId());
    }

    public void testSavePlaylist() {
        Playlist playlist = new Playlist("My favourites", 5);
        long id = mPlaylistDAO.savePlaylist(playlist);
        assertTrue(id != 0);
    }

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