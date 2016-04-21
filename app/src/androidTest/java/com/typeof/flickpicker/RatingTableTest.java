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

    SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        db = mDbHelper.getWritableDatabase();
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTableCreation() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'ratings'", null);
        //System.out.println(cursor);
        assertNotNull(cursor);
    }

    public void testRecordInsertion() {

        // Create a new map of values, where column names are the keys
        ContentValues ratingValues = new ContentValues();
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_ID, 2);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_RATING, 3.0);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_MOVIEID, 4);
        ratingValues.put(RatingTable.RatingEntry.COLUMN_NAME_USERID, 4);

        long newRowIdRating;
                newRowIdRating = db.insert(
                RatingTable.RatingEntry.TABLE_NAME,
                RatingTable.RatingEntry.COLUMN_NAME_NULLABLE,
                ratingValues);

        assertEquals(2, newRowIdRating);

        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
    }

    public void testTableDeletion() {
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'ratings'", null);
        assertTrue(cursor.getCount() == 0);

    }
}