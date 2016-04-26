package com.typeof.flickpicker.database.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typeof.flickpicker.core.DatabaseObject;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.database.PlaylistDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public class SQLPlaylistDAO extends SQLDAO implements PlaylistDAO {
    public SQLPlaylistDAO(Context ctx) {
        super(ctx);
    }

    @Override
    public long savePlaylist(Playlist playlist) {
        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(PlaylistTable.PlaylistEntry.COLUMN_NAME_TITLE, playlist.getTitle());
        values.put(PlaylistTable.PlaylistEntry.COLUMN_NAME_USER_ID, playlist.getUserId());
        values.put(PlaylistTable.PlaylistEntry.COLUMN_NAME_MOVIES_LIST, gson.toJson(playlist.getMovieIds()));
        return super.save(playlist, "playlists", values);
    }

    @Override
    public Playlist findPlaylistById(long id) {
        Cursor c = super.find(id, "playlists");
        c.moveToFirst();
        Playlist playlist = createPlaylistFromCursor(c);
        c.close();
        return playlist;
    }

    private Playlist createPlaylistFromCursor(Cursor c) {
        Gson gson = new Gson();
        long id = c.getLong(c.getColumnIndex(PlaylistTable.PlaylistEntry.COLUMN_NAME_ID));
        String title = c.getString(c.getColumnIndex(PlaylistTable.PlaylistEntry.COLUMN_NAME_TITLE));
        long userId = c.getLong(c.getColumnIndex(PlaylistTable.PlaylistEntry.COLUMN_NAME_USER_ID));
        String moviesListJSON = c.getString(c.getColumnIndex(PlaylistTable.PlaylistEntry.COLUMN_NAME_MOVIES_LIST));
        List<Number> moviesFromDB = gson.fromJson(moviesListJSON, new TypeToken<ArrayList<Number>>(){}.getType());

        Playlist playlist = new Playlist(title, userId, moviesFromDB);

        playlist.setId(id);

        return playlist;

    }

    @Override
    public List<Playlist> getUserPlaylists(long userId) {
        return null;
    }
}
