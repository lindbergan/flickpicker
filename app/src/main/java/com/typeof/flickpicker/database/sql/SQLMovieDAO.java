package com.typeof.flickpicker.database.sql;
import android.content.Context;
import android.provider.BaseColumns;

import com.typeof.flickpicker.database.MovieDAO;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */
public class SQLMovieDAO extends SQLDAO implements MovieDAO {
    public SQLMovieDAO(Context ctx) {
        super(ctx);
    }
}
