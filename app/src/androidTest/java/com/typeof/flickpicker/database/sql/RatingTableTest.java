package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.sql.tables.RatingTable;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */

public class RatingTableTest extends AndroidTestCase {

    private SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        db = mDbHelper.getWritableDatabase();
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());
        db.execSQL(RatingTable.RatingEntry.getSQLCreateTableQuery());
    }

    // Tests that the ratings table exists

    public void testTableCreation() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{RatingTable.RatingEntry.TABLE_NAME});
        assertNotNull(cursor);
        cursor.close();
    }

    /**
     * Tests that inserting values in to the ratings table works
     * Uses Map for the values in the columns
     */

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

    // Tests that the ratings table doesn't exist

    public void testTableDeletion() {
        db.execSQL(RatingTable.RatingEntry.getSQLDropTableQuery());

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{RatingTable.RatingEntry.TABLE_NAME});
        int count = cursor.getCount();
        cursor.close();
        assertTrue(count == 0);
    }
}