package com.typeof.flickpicker.database.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.typeof.flickpicker.App;
import com.typeof.flickpicker.database.Database;

import junit.framework.TestCase;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-26.
 */
public class PlaylistTableTest extends AndroidTestCase {

    private Database mDatabase;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDatabase = App.getDatabaseSeed();
        mDatabase.setUpTables();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabase.dropTables();
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