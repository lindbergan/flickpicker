package com.typeof.flickpicker.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.database.sql.PlaylistTable;
import com.typeof.flickpicker.database.sql.SQLPlaylistDAO;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-26.
 */
public class PlaylistDAOTest extends AndroidTestCase {

    private PlaylistDAO mPlaylistDAO;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mPlaylistDAO = App.getPlaylistDAO();
        SQLiteDatabaseHelper databaseHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLDropTableQuery());
        db.execSQL(PlaylistTable.PlaylistEntry.getSQLCreateTableQuery());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetUserPlaylists() {
        Playlist playlist = new Playlist("My favourites", 8, new ArrayList<Number>(){{add(20); add(30); add(40);}});
        long id1 = mPlaylistDAO.savePlaylist(playlist);
        Playlist playlist2 = new Playlist("My worst movies", 8, new ArrayList<Number>(){{add(20); add(30); add(40);}});
        long id2 = mPlaylistDAO.savePlaylist(playlist2);
        Playlist playlist3 = new Playlist("My coolest movies", 8, new ArrayList<Number>(){{add(20); add(30); add(40);}});
        long id3 = mPlaylistDAO.savePlaylist(playlist3);

        List<Playlist> playlists = mPlaylistDAO.getUserPlaylists(8);

        assertEquals(3, playlists.size());
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
        id = mPlaylistDAO.removePlaylist(playlist);
        assertTrue(id != 0);
    }
}