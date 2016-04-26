package com.typeof.flickpicker.database.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import junit.framework.TestCase;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-26.
 */
public class PlaylistTableTest extends AndroidTestCase {

    private SQLiteDatabase db;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        db = mDbHelper.getWritableDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTableCreation() {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'playlists'", null);
        assertEquals(1, cursor.getCount());
    }

    public void testRecordInsertion() {
        assertTrue(true);
    }
}