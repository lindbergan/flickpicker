package com.typeof.flickpicker.database.sql;
import android.content.Context;

import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.database.PlaylistDAO;

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
    public Playlist findPlaylistById(long id) {
        return null;
    }

    @Override
    public List<Playlist> getUserPlaylists(long userId) {
        return null;
    }
}
