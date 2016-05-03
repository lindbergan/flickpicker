package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.sql.MovieTable;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieTableTest extends AndroidTestCase {

    SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        db = mDbHelper.getWritableDatabase();
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());
        db.execSQL(MovieTable.MovieEntry.getSQLCreateTableQuery());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTableCreation() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'movies'", null);
        assertTrue(cursor.getCount() == 1);
    }

    public void testRecordInsertion() {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MovieTable.MovieEntry.COLUMN_NAME_ID, 5);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_TITLE, "Shawshank Redemption");
        values.put(MovieTable.MovieEntry.COLUMN_NAME_YEAR, 1994);
        values.put(MovieTable.MovieEntry.COLUMN_NAME_DESCRIPTION, "Derp");
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

    public void testTableDeletion() {
        db.execSQL(MovieTable.MovieEntry.getSQLDropTableQuery());

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'movies'", null);
        assertTrue(cursor.getCount() == 0);
    }

}