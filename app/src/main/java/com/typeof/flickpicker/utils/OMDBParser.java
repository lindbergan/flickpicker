package com.typeof.flickpicker.utils;
import android.os.AsyncTask;
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
public class OMDBParser extends AsyncTask<Void, Void, List<Movie>> {

    private List<Movie> movies = new ArrayList<>();
    private List<String> movieIds = new ArrayList<>();
    public static final int NUM_MOVIES = 35;

    @Override
    protected List<Movie> doInBackground(Void... params) {
        for (String imdbId : movieIds) {
            try {
                Movie movie = requestMovieFromOMDB(imdbId);
                movies.add(movie);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }
        return movies;
    }

    public OMDBParser() {
        movieIds.add("tt0111161");
        movieIds.add("tt0068646");
        movieIds.add("tt0071562");
        movieIds.add("tt0468569");
        movieIds.add("tt0108052");
        movieIds.add("tt0050083");
        movieIds.add("tt0110912");
        movieIds.add("tt0167260");
        movieIds.add("tt0060196");
        movieIds.add("tt0137523");
        movieIds.add("tt0120737");
        movieIds.add("tt0080684");
        movieIds.add("tt0109830");
        movieIds.add("tt1375666");
        movieIds.add("tt0167261");
        movieIds.add("tt0073486");
        movieIds.add("tt0099685");
        movieIds.add("tt0133093");
        movieIds.add("tt0047478");
        movieIds.add("tt0076759");
        movieIds.add("tt0317248");
        movieIds.add("tt0114369");
        movieIds.add("tt0102926");
        movieIds.add("tt0038650");
        movieIds.add("tt0114814");
        movieIds.add("tt0118799");
        movieIds.add("tt0110413");
        movieIds.add("tt0064116");
        movieIds.add("tt0245429");
        movieIds.add("tt0120815");
        movieIds.add("tt0120586");
        movieIds.add("tt0816692");
        movieIds.add("tt0034583");
        movieIds.add("tt0021749");
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

            return new Movie(title, plot, year, genre, poster);

        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(List list) {
        super.onPostExecute(list);
    }
}
