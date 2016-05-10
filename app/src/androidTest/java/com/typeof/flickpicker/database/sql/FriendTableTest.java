package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.sql.FriendTable;
import com.typeof.flickpicker.database.sql.MovieTable;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class FriendTableTest extends AndroidTestCase {

    SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        db = mDbHelper.getWritableDatabase();
        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());
        db.execSQL(FriendTable.FriendEntry.getSQLCreateTableQuery());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTableCreation() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'friends'", null);
        assertTrue(cursor.getCount() == 1);
        cursor.close();
    }

    public void testTableDeletion() {
        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'friends'", null);
        assertTrue(cursor.getCount() == 0);
        cursor.close();
    }

}