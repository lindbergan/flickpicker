package com.typeof.flickpicker.database.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.sql.tables.FriendTable;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */

public class FriendTableTest extends AndroidTestCase {

    SQLiteDatabase db;

    /**
     * Resets Friends table
     * @throws Exception
     */

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

    // Tests that the friends table exists

    public void testTableCreation() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{FriendTable.FriendEntry.TABLE_NAME});
        assertTrue(cursor.getCount() == 1);
        cursor.close();
    }

    // Tests that the friends table doesn't exist

    public void testTableDeletion() {
        db.execSQL(FriendTable.FriendEntry.getSQLDropTableQuery());

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{FriendTable.FriendEntry.TABLE_NAME});
        assertTrue(cursor.getCount() == 0);
        cursor.close();
    }

}