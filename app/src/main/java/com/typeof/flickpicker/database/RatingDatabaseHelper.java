package com.typeof.flickpicker.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.core.CoreEntity;
import com.typeof.flickpicker.core.Movie;
import com.typeof.flickpicker.core.Rating;

import java.util.ArrayList;
import java.util.List;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-21.
 */
public class RatingDatabaseHelper extends DatabaseHelper<Movie> {

    public RatingDatabaseHelper(Context ctx) {
        super(ctx);
    }

    public Rating find(long id) {
        try {
            Cursor c = super.find(id, "ratings");
            Rating rating = createRatingCursor(c);
            c.close();
            return rating;
        } catch (DatabaseRecordNotFoundException e) {
            throw new DatabaseRecordNotFoundException(e.getMessage());
        }
    }

    public Rating createRatingCursor(Cursor c) {
        c.moveToFirst();
        long id = c.getLong(c.getColumnIndex(MovieTable.MovieEntry.COLUMN_NAME_ID));
        Double rating = c.getDouble(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_RATING));
        int movieId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID));
        int userId = c.getInt(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_USERID));
        return new Rating(id, rating, movieId,userId);
    }

    public long save(Rating rating) {
        ContentValues values = new ContentValues();
        values.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, rating.getRating());

        return super.save(rating, "ratings", values);
    }

    /*
    public void update(Rating rating, ContentValues values) {
        super.update(rating, values);
    }
    */

    @Override
    public void update(CoreEntity object, ContentValues values) {
        String selection = "id LIKE ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        int count = this.getDatabase().update(
                RatingTable.RatingEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public long delete(Rating rating) {
        super.delete(rating, "ratings");
        return rating.getId();
    }

    public List<Rating> search(String searchString) {
        List<Rating> results = new ArrayList<>();
        Cursor c = super.search("ratings","rating", searchString); //will fail... how to solve? - can we really have search as generic?

        while (c.moveToNext()) {
            long movieId = c.getLong(c.getColumnIndex(RatingTable.RatingEntry.COLUMN_NAME_ID));
            results.add(createRatingCursor(c));
        }

        c.close();

        return results;
    }
}
