package com.typeof.flickpicker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.MovieTable;
import com.typeof.flickpicker.database.SQLiteDatabaseHelper;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieTableTest extends AndroidTestCase {

    public void testTableCreation() {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'movies'", null);
        System.out.println(cursor);
        assertNotNull(cursor);
    }

    public void testRecordInsertion() {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_ID, 5);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, "Shawshank Redemption");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_DESCRIPTION, "Derp");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_GENRE, "Drama");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_VOTES, 10);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING, 5.5);

        long newRowId;
        newRowId = db.insert(
                MovieTable.MovieEntry.TABLE_NAME,
                MovieTable.MovieEntry.COLUMN_NAME_NULLABLE,
                values);

        assertEquals("Record created in database", 5, newRowId);

        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
    }

    public void testTableDeletion() {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

    }

}