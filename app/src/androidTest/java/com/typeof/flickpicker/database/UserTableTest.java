package com.typeof.flickpicker.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.MovieTable;
import com.typeof.flickpicker.database.SQLiteDatabaseHelper;

import junit.framework.TestCase;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */
public class UserTableTest extends AndroidTestCase {

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
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'users'", null);
        assertTrue(cursor.getCount() == 1);
    }


}