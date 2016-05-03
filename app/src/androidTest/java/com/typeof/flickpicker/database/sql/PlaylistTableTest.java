package com.typeof.flickpicker.database.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

<<<<<<< HEAD
import com.typeof.flickpicker.App;
import com.typeof.flickpicker.database.Database;

import junit.framework.TestCase;
=======
import com.typeof.flickpicker.activities.App;
import com.typeof.flickpicker.database.Database;
>>>>>>> master

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-26.
 */
public class PlaylistTableTest extends AndroidTestCase {

<<<<<<< HEAD
    private Database mDatabase;
=======
    private Database db;
>>>>>>> master

    @Override
    protected void setUp() throws Exception {
        super.setUp();
<<<<<<< HEAD
        mDatabase = App.getDatabaseSeed();
        mDatabase.setUpTables();
=======
        db = App.getDatabaseSeed();
        db.setUpTables();
>>>>>>> master
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
<<<<<<< HEAD
        mDatabase.dropTables();
=======
        db.dropTables();
>>>>>>> master
    }

    public void testTableCreation() {
        SQLiteDatabaseHelper mDbHelper = new SQLiteDatabaseHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'playlists'", null);
        assertEquals(1, cursor.getCount());
    }
}