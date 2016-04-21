package com.typeof.flickpicker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.typeof.flickpicker.core.CoreEntity;
import com.typeof.flickpicker.core.Movie;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */
public abstract class DatabaseHelper<T> {

    private SQLiteDatabase db;

    public DatabaseHelper(Context ctx) {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(ctx);
        db = mDbHelper.getWritableDatabase();
    }

    public Cursor find(long id, String tableName) {
        return db.rawQuery("SELECT * FROM ? WHERE id = ? LIMIT 1", new String[]{tableName, String.valueOf(id)});
    }

    public long save(CoreEntity object, String tableName, ContentValues values) {
        // If we have an id on this object
        // Check if it exists in the database
        if (object.getId() != 0) {
            // if it does, then we update the existing record
            update(object, values);
            return object.getId();
        }

        long newRowId = db.insert(tableName, MovieTable.MovieEntry.COLUMN_NAME_NULLABLE, values);

        // If there was an error saving the record, we return -1 as the id
        if (newRowId == -1) return -1;

        object.setId(newRowId);

        return newRowId;
    }

    public void update(CoreEntity object, ContentValues values) {
        String selection = "id LIKE ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        int count = db.update(
                MovieTable.MovieEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public long delete(CoreEntity object) throws IllegalStateException {
        if (object.getId() == 0) {
            throw new IllegalStateException("Core Entity cannot be deleted before it has been saved to the database");
        }

        return object.getId();
    }



}
