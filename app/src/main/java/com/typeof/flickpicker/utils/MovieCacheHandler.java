package com.typeof.flickpicker.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-27.
 */
public class MovieCacheHandler extends AsyncTask<Void, Void, Void> {

    private final Context ctx;
    private OnTaskCompleted mOnTaskCompleted;
    private ProgressDialog mProgressDialog;

    public MovieCacheHandler(Context context) {
        ctx = context;
        mProgressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog.setMessage("Seeding database... hold on!");
        mProgressDialog.show();
    }

    public MovieCacheHandler(Context context, OnTaskCompleted onTaskCompleted) {
        ctx = context;
        mOnTaskCompleted = onTaskCompleted;
    }

    public static void saveMoviesToDisk(Context ctx) {
        String fileName = "movies.txt";
        MovieDAO movieDAO = App.getMovieDAO();
        List<Movie> movieList = movieDAO.getCommunityTopPicks(500);

        try {
            File file = new File(ctx.getFilesDir(), fileName);

            if(!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Cannot create file!");
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(movieList);
            outputStream.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void insertMoviesFromDisc() {
        MovieDAO movieDAO = App.getMovieDAO();
        List<Movie> movies = new ArrayList<>();

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(ctx.getAssets().open("movies.txt"));
            movies = (List<Movie>)objectInputStream.readObject();
            objectInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Movie movie : movies) {
            movie.setId(0); // we need to reset the movie's id
            movieDAO.saveMovie(movie);
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        insertMoviesFromDisc();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgressDialog.hide();
        if (mOnTaskCompleted != null) {
            mOnTaskCompleted.onTaskCompleted();
        }
    }
}
