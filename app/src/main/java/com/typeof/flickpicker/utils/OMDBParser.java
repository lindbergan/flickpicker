package com.typeof.flickpicker.utils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.typeof.flickpicker.core.Movie;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-17.
 */
public class OMDBParser {

    private List<Movie> movies = new ArrayList<>();

    private List<String> movieIds = new ArrayList<>();

    public OMDBParser() {
        movieIds.add("tt0111161");
        movieIds.add("tt0068646");
        movieIds.add("tt0071562");
    }

    public void setMovieIds(List<String> movieIds) {
        this.movieIds = movieIds;
    }

    public void requestAllMoviesFromOMDB() {
        for (String imdbId : movieIds) {
            try {
                Movie movie = requestMovieFromOMDB(imdbId);
                movies.add(movie);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    private Movie requestMovieFromOMDB(String imdbId) throws Exception {

        try {
            URL url = new URL("http://www.omdbapi.com/?i="+imdbId+"&plot=short&r=json");
            InputStream input = url.openStream();
            Map<String, String> map = new Gson().fromJson(new InputStreamReader(input, "UTF-8"), new TypeToken<Map<String, String>>() {
            }.getType());

            String title = map.get("Title");
            int year = Integer.parseInt(map.get("Year"));
            String genre = map.get("Genre");
            String plot = map.get("Plot");
            String poster = map.get("Poster");

            input.close();

            return new Movie(title, plot, year, genre);

        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            throw new Exception(e.getMessage());
        }

    }
}
