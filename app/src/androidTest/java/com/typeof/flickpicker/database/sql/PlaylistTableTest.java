package com.typeof.flickpicker.database.sql;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import com.typeof.flickpicker.database.Database;
import com.typeof.flickpicker.activities.App;

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
        mDatabase = App.getDatabase();
        mDatabase.setUpTables();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDatabase.dropTables();
    }

    public void testTableCreation() {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = 'playlists'", null);
        assertEquals(1, cursor.getCount());
    }
}