package com.typeof.flickpicker.database.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.typeof.flickpicker.core.DatabaseObject;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */
public abstract class SQLDAO {

    private SQLiteDatabase db;

    public SQLDAO(Context ctx) {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(ctx);
        db = mDbHelper.getWritableDatabase();
    }

    public Cursor find(long id, String tableName) throws DatabaseRecordNotFoundException {
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE id = ? LIMIT 1", new String[]{String.valueOf(id)});
        if (cursor.getCount() == 0) {
            throw new DatabaseRecordNotFoundException("Record not found in database. UserId: " + id);
        }
        return cursor;
    }

    public long save(DatabaseObject object, String tableName, ContentValues values) {
        // If we have an id on this object
        // Check if it exists in the database

        if (object.getId() != 0) {
            try {
                this.find(object.getId(), tableName);
                update(object, values, tableName);
                return object.getId();
            } catch (DatabaseRecordNotFoundException e) {   }
        }

        long newRowId = db.insert(tableName, " ", values);

        // If there was an error saving the record, we return -1 as the id
        if (newRowId == -1) return -1;

        object.setId(newRowId);


        return newRowId;
    }

    public void update(DatabaseObject object, ContentValues values, String tableName) {
        String selection = "id LIKE ?";
        String[] selectionArgs = { String.valueOf(object.getId()) };

        try {
            db.beginTransaction();
            db.update(
                    tableName,
                    values,
                    selection,
                    selectionArgs);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Delete
     * Returns number of rows affected
     * @param object
     * @param tableName
     * @return
     * @throws IllegalStateException
     */

    public int delete(DatabaseObject object, String tableName) throws IllegalStateException {
        if (object.getId() == 0) {
            throw new IllegalStateException("Core Entity cannot be deleted before it has been saved to the database");
        }

        return db.delete(tableName, "id = " + object.getId(), null);


    }

    public Cursor search(String tableName, String column, String searchString) {
        return db.rawQuery("SELECT * FROM " + tableName + " WHERE " + column + " LIKE ?",
                new String[]{"%" + searchString + "%"});
    }
    
    public SQLiteDatabase getDatabase(){
        return db;
    }
}
