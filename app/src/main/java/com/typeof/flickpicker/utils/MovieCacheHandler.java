package com.typeof.flickpicker.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.database.MovieDAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-27.
 */
public class MovieCacheHandler {


    public static void saveMoviesToDisk(Context ctx) {
        MovieDAO movieDAO = App.getMovieDAO();

        List<Movie> movieList = movieDAO.getCommunityTopPicks(500);

        try {
            FileOutputStream fileOutputStream = ctx.openFileOutput("movies.txt", Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }


}
