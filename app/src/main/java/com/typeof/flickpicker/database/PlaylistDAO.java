package com.typeof.flickpicker.database;
import com.typeof.flickpicker.core.Playlist;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface PlaylistDAO {
    Playlist findPlaylistById(long id);
    List<Playlist> getUserPlaylists(long userId);
}
