package com.typeof.flickpicker.database.sql.DAO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.sql.CoreEntityFactory;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;
import com.typeof.flickpicker.database.sql.tables.PlaylistTable;

import java.util.ArrayList;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public class SQLPlaylistDAO extends SQLDAO implements PlaylistDAO {

    private SQLiteDatabase db;

    public SQLPlaylistDAO(Context ctx) {
        super(ctx);
        SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance(ctx);
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
        if (c.getCount() > 0) {
            Playlist playlist = CoreEntityFactory.createPlaylistFromCursor(c);
            c.close();
            return playlist;
        } else {
            c.close();
            return null;
        }
    }



    @Override
    public Playlist getPlaylist(long userId) {

        String query = "select * from playlists where playlists.user_id = ?";

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(userId)});
        c.moveToFirst();
        if (c.getCount() > 0) {
            Playlist p = CoreEntityFactory.createPlaylistFromCursor(c);
            c.close();
            return p;
        } else {
            c.close();
            return null;
        }
    }

    /**
     * Deletes the playlist
     * @param playlist
     * @return
     */

    @Override
    public long removePlaylist(Playlist playlist) {

        return super.delete(playlist, PlaylistTable.PlaylistEntry.TABLE_NAME);

    }
}
