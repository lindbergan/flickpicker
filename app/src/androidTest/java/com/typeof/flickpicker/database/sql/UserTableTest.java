package com.typeof.flickpicker.database.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.typeof.flickpicker.database.sql.tables.UserTable;

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
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        db = mDbHelper.getWritableDatabase();
        db.execSQL(UserTable.UserEntry.getSQLDropTableQuery());
        db.execSQL(UserTable.UserEntry.getSQLCreateTableQuery());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // Tests that the ratings table exists

    public void testTableCreation() {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{UserTable.UserEntry.TABLE_NAME});
        assertNotNull(cursor);
        cursor.close();
    }

    // Tests that the ratings table doesn't exist

    public void testTableDeletion() {
        db.execSQL(UserTable.UserEntry.getSQLDropTableQuery());

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{UserTable.UserEntry.TABLE_NAME});
        int count = cursor.getCount();
        cursor.close();
        assertTrue(count == 0);
    }
}