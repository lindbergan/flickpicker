package com.typeof.flickpicker.database;
import com.typeof.flickpicker.core.Playlist;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface PlaylistDAO {
    long savePlaylist(Playlist playlist);
    Playlist findPlaylistById(long id);
    Playlist getPlaylist(long userId);
    long removePlaylist(Playlist playlist);
}
