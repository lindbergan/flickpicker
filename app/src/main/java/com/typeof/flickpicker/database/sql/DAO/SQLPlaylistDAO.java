package com.typeof.flickpicker.database.sql.DAO;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.User;
import com.typeof.flickpicker.database.PlaylistDAO;
import com.typeof.flickpicker.database.sql.CoreEntityFactory;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;
import com.typeof.flickpicker.database.sql.tables.PlaylistTable;

import java.util.ArrayList;

/**
 * SQLPlaylistDAO
 * DAO for Playlist objects
 */
public class SQLPlaylistDAO extends SQLDAO implements PlaylistDAO {

    private SQLiteDatabase db;

    public SQLPlaylistDAO(Context ctx) {
        super(ctx);
        SQLiteDatabaseHelper databaseHelper = SQLiteDatabaseHelper.getInstance(ctx);
        this.db = databaseHelper.getReadableDatabase();
    }

    /**
     * Saves playlist object to database
     *
     * @param playlist  Playlist object
     * @return          Id of record saved in database
     */
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

    /**
     * Returns Playlist object found in database
     *
     * @param id    id of playlist in database
     * @return      Playlist object
     */
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

    /**
     * Returns Playlist belonging to user
     *
     * @param userId    Given user ids
     * @return          Playlist object
     */
    public Playlist getUserPlaylist(long userId) {
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

     * @param playlist  Playlist object
     * @return          number of rows affected in the database
     */
    public long removePlaylist(Playlist playlist) {
        return super.delete(playlist, PlaylistTable.PlaylistEntry.TABLE_NAME);
    }


    /**
     * method for adding a moie to the user's playlist
     * @param user
     * @param movie
     */
    @Override
    public void addMovieToPlaylist(User user, Movie movie){

        long userId = user.getId();
        long movieId = movie.getId();

        Playlist playlist = getUserPlaylist(userId);
        if(playlist == null){
            playlist = new Playlist("Watchlist", userId);
            savePlaylist(playlist);
        }

        playlist.add(movieId);
        savePlaylist(playlist);
    }


    /**
     * method for removing a movie from the user's playlist
     * @param user
     * @param movie
     */
    public void removeMovieFromPlaylist(User user, Movie movie){

        long userId = user.getId();
        long movieId = movie.getId();

        Playlist playlist = getUserPlaylist(userId);

            if(playlist == null){
                return;
            }
        System.out.println("Innan: " + playlist.getMovieIds().size());
        playlist.remove(movieId);
        System.out.println("Efter: " + playlist.getMovieIds().size());
        savePlaylist(playlist);
        }

    }

