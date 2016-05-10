package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;
import com.typeof.flickpicker.database.sql.UserTable;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */
public class UserTableTest extends AndroidTestCase {

    public void testTableCreation() {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'movies'", null);
        System.out.println(cursor);
        assertNotNull(cursor);
    }

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

    public void testTableDeletion() {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();


    }



}