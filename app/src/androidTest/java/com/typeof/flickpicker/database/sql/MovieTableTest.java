package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.sql.tables.MovieTable;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieTableTest extends AndroidTestCase {

    private SQLiteDatabase db;

    /**
     * Resets Movies table
     * @throws Exception
     */

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        db = mDbHelper.getWritableDatabase();
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());
    }

    // Tests that the friends table exists

    public void testTableCreation() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{MovieTable.MovieEntry.TABLE_NAME});
        int count = cursor.getCount();
        cursor.close();
        assertTrue(count == 1);
    }

    /**
     * Tests that inserting values in to the movies table works
     * Uses Map for the values in the columns
     */

    public void testRecordInsertion() {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_ID, 5);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, "Shawshank Redemption");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_YEAR, 1994);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_DESCRIPTION, "Description");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_GENRE, "Drama");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_VOTES, 10);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_COMMUNITY_RATING, 5.5);

        long newRowId = db.insert(
                MovieTable.MovieEntry.TABLE_NAME,
                MovieTable.MovieEntry.COLUMN_NAME_NULLABLE,
                values);

        assertEquals(5, newRowId);

        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
    }

    // Tests that the movies table doesn't exist

    public void testTableDeletion() {
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{MovieTable.MovieEntry.TABLE_NAME});
        int count = cursor.getCount();
        cursor.close();
        assertTrue(count == 0);
    }

}