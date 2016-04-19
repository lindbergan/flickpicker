package com.typeof.flickpicker.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.typeof.flickpicker.Movie;
/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieDatabaseHelper {
    private SQLiteDatabase db;

    public MovieDatabaseHelper(Context ctx) {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(ctx);
        db = mDbHelper.getWritableDatabase();
    }

    public Movie find(long id) {
        Cursor c = db.rawQuery("SELECT * FROM movies WHERE id = ? LIMIT 1", new String[]{String.valueOf(id)});
        if (c.getCount() < 1) {
            return null;
        }
        c.moveToFirst();
        String movieTitle = c.getString(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_TITLE));
        c.close();
        return new Movie(movieTitle);
    }

    public long save(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());

        // If we have an id on this object
        // Check if it exists in the database
        if (movie.getId() != 0 && this.find(movie.getId()) != null) {
            // if it does, then we update the existing record
            update(movie, values);
            return movie.getId();
        }

        long newRowId;
        newRowId = db.insert(MovieTable.MovieEntry.TABLE_NAME, MovieTable.MovieEntry.COLUMN_NAME_NULLABLE, values);

        // If there was an error saving the record, we return -1 as the id
        if (newRowId == -1) return -1;

        movie.setId(newRowId);

        return newRowId;
    }

    private void update(Movie movie, ContentValues values) {
        String selection = MovieTable.MovieEntry.COLUMN_NAME_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(movie.getId()) };

        int count = db.update(
                MovieTable.MovieEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public long delete(Movie movie) {
        return movie.getId();
    }
}
