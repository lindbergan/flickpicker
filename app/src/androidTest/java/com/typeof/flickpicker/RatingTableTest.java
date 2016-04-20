package com.typeof.flickpicker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.MovieTable;
import com.typeof.flickpicker.database.RatingTable;
import com.typeof.flickpicker.database.SQLiteDatabaseHelper;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class RatingTableTest extends AndroidTestCase {

    private SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseHelper rDbHelper = new SQLiteDatabaseHelper(getContext());
        db = rDbHelper.getWritableDatabase();
    }



    public void testTableCreation() {
        //SQLiteDatabaseHelper rDbHelper = new SQLiteDatabaseHelper(getContext());
        //SQLiteDatabase db = rDbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'ratings'", null);
        System.out.println(cursor);
        assertNotNull(cursor);
    }

    public void testRecordInsertion() {
        //SQLiteDatabaseHelper rDbHelper = new SQLiteDatabaseHelper(getContext());
        //SQLiteDatabase db = rDbHelper.getWritableDatabase();

        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());

        // Create a new map of values, where column names are the keys
        ContentValues ratingValues = new ContentValues();
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_ID, 2);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, 3.0);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, 4);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, 4);

        long newRowIdRating = db.insert(
                MovieTable.MovieEntry.TABLE_NAME,
                MovieTable.MovieEntry.COLUMN_NAME_NULLABLE,
                ratingValues);

        assertEquals("Record created in database", 2, newRowIdRating);

        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
    }

    public void testTableDeletion() {
        SQLiteDatabaseHelper rDbHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = rDbHelper.getWritableDatabase();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}