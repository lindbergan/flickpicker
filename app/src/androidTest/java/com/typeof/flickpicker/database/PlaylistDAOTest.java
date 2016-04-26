package com.typeof.flickpicker.database;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.database.sql.PlaylistTable;
import com.typeof.flickpicker.database.sql.SQLPlaylistDAO;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;

import java.util.ArrayList;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-26.
 */
public class PlaylistDAOTest extends AndroidTestCase {

    private PlaylistDAO mPlaylistDAO;
    private SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseHelper databaseHelper = new SQLiteDatabaseHelper(getContext());
        mPlaylistDAO = new SQLPlaylistDAO(getContext());
        db = databaseHelper.getWritableDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFindPlaylistById() {
        Playlist playlist = new Playlist("My favourites", 5);
        long id = mPlaylistDAO.savePlaylist(playlist);

        Playlist foundPlaylist = mPlaylistDAO.findPlaylistById(id);
        assertEquals(id, foundPlaylist.getId());
    }

    public void testSavePlaylist() {
        Playlist playlist = new Playlist("My favourites", 5);
        long id = mPlaylistDAO.savePlaylist(playlist);
        assertTrue(id != 0);
    }
}