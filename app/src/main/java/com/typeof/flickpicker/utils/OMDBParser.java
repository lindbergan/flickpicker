package com.typeof.flickpicker.utils;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OMDBParser
 * Fetches JSON objects from the OMDB Api
 */
public class OMDBParser extends AsyncTask<Void, Void, List<Movie>> {

    private final List<Movie> movies = new ArrayList<>();
    private List<String> movieIds = new ArrayList<>();
    private final Context ctx;
    private final ProgressDialog mProgressDialog;
    private final MovieDAO mMovieDAO;

    public OMDBParser(Context ctx, MovieDAO movieDAO) {
        this.ctx = ctx;
        readTopListFile();
        this.mMovieDAO = movieDAO;
        mProgressDialog = new ProgressDialog(ctx);
    }

    /**
     * Method that is preformed async to the main thread
     * @param params
     * @return
     */
    @Override
    protected List<Movie> doInBackground(Void... params) {
        for (String imdbId : movieIds) {
            try {
                Movie movie = requestMovieFromOMDB(imdbId);
                mMovieDAO.saveMovie(movie);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }
        return movies;
    }

    /**
     * Read IMDB top list ids from text file.
     */
    private void readTopListFile() {
        try {
            AssetManager am = ctx.getAssets();
            InputStream is = am.open("movieIds.txt");

            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            while( (line = bufferedReader.readLine()) != null )
            {
                movieIds.add(line);
            }

            bufferedReader.close();

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public void setMovieIds(List<String> movieIds) {
        this.movieIds = movieIds;
    }

    /**
     * Request All Movies from OMDB
     */
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

    /**
     * Displays dialog before the async task runs
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Seeding database... hold on!");
        mProgressDialog.show();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    /**
     * Sends request to the OMDB API, returns Movie instance
     * @param imdbId
     * @return
     * @throws Exception
     */
    private Movie requestMovieFromOMDB(String imdbId) throws Exception {

        try {
            // Data is returned as JSON
            URL url = new URL("http://www.omdbapi.com/?i="+imdbId+"&plot=short&r=json");
            InputStream input = url.openStream();
            Map<String, String> map = new Gson().fromJson(new InputStreamReader(input, "UTF-8"), new TypeToken<Map<String, String>>() {
            }.getType());

            String title = map.get("Title");
            int year = Integer.parseInt(map.get("Year"));
            String genre = map.get("Genre");
            String plot = map.get("Plot");
            String poster = map.get("Poster");
            String imdbRating = map.get("imdbRating");
            String imdbVotes = map.get("imdbVotes");

            // Imdb rating (1 - 10) divides by 2 because we use 1 - 5 rating
            double communityRating = Double.parseDouble(imdbRating) / 2;
            int nrOfVotes = Integer.parseInt(imdbVotes.replace(",", ""));

            input.close();

            Movie movie = new Movie(title, plot, year, genre, poster);
            movie.setCommunityRating(communityRating);
            movie.setNumberOfVotes(nrOfVotes);

            return movie;

        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            throw new Exception(e.getMessage());
        }

    }

    /**
     * After async task is 
     * @param list
     */
    @Override
    protected void onPostExecute(List list) {
        super.onPostExecute(list);
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
