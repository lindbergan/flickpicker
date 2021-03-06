package com.typeof.flickpicker.database;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Playlist;
import com.typeof.flickpicker.core.User;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public interface PlaylistDAO {
    long savePlaylist(Playlist playlist);
    Playlist findPlaylistById(long id);
    Playlist getUserPlaylist(long userId);
    long removePlaylist(Playlist playlist);
    void addMovieToPlaylist(User user, Movie movie);
    void removeMovieFromPlaylist(User user, Movie movie);
    boolean isMovieOnPlaylist(User user, Movie movie);
}
