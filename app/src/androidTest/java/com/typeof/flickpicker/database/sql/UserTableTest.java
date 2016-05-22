package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
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

    // Tests that the users table exists

    public void testTableCreation() {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{UserTable.UserEntry.TABLE_NAME});
        System.out.println(cursor);
        assertNotNull(cursor);
    }

    /**
     * Tests that inserting values in to the users table works
     * Uses Map for the values in the columns
     */

    public void testRecordInsertion() {

        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL(UserTable.UserEntry.getSQLDropTableQuery());
        db.execSQL(UserTable.UserEntry.getSQLCreateTableQuery());

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(UserTable.UserEntry.COLUMN_NAME_ID, 1);
        values.put(UserTable.UserEntry.COLUMN_NAME_USERNAME, "gud1sunk1");
        values.put(UserTable.UserEntry.COLUMN_NAME_PASSWORD, "jolo2016");
        values.put(UserTable.UserEntry.COLUMN_NAME_SCORE, "1337");

        long newRowId;
        newRowId = db.insert(UserTable.UserEntry.TABLE_NAME,
                UserTable.UserEntry.COLUMN_NAME_NULLABLE,
                values);

        assertEquals("Record created in database", 1, newRowId);

    }

    // Tests that the users table doesn't exist

    public void testTableDeletion() {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UserTable.UserEntry.getSQLDropTableQuery());

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = ?", new String[]{UserTable.UserEntry.TABLE_NAME});
        int count = cursor.getCount();
        cursor.close();
        assertTrue(count == 0);


    }



}