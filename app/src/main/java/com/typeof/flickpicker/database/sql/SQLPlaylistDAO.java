package com.typeof.flickpicker.database.sql;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    private SQLiteDatabase db;

    public SQLPlaylistDAO(Context ctx) {
        super(ctx);
        SQLiteDatabaseHelper databaseHelper = new SQLiteDatabaseHelper(ctx);
        this.db = databaseHelper.getReadableDatabase();
    }


    @Override
    public long savePlaylist(Playlist playlist) {
        Gson gson = new Gson();
        String movieIdsJson = gson.toJson(playlist.getMovieIds(), new TypeToken<ArrayList<Number>>() {
        }.getType());
        ContentValues values = new ContentValues();
        values.put(PlaylistTable.PlaylistEntry.COLUMN_NAME_TITLE, playlist.getTitle());
        values.put(PlaylistTable.PlaylistEntry.COLUMN_NAME_USER_ID, playlist.getUserId());
        values.put(PlaylistTable.PlaylistEntry.COLUMN_NAME_MOVIES_LIST, movieIdsJson);
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
        List<Playlist> playlists = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM playlists WHERE user_id = ?", new String[]{String.valueOf(userId)});
        c.moveToFirst();
        try {
            do {
                playlists.add(createPlaylistFromCursor(c));
            }
            while(c.moveToNext());
        } finally {
            c.close();
        }
        return playlists;
    }
}
