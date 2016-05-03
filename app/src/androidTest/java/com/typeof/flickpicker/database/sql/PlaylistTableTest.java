package com.typeof.flickpicker.database.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.database.Database;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-26.
 */
public class PlaylistTableTest extends AndroidTestCase {

    private Database db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        db = App.getDatabaseSeed();
        db.setUpTables();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        db.dropTables();
    }

    public void testTableCreation() {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'playlists'", null);
        assertEquals(1, cursor.getCount());
    }
}