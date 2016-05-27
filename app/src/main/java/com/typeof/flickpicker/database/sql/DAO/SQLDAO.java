package com.typeof.flickpicker.database.sql.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.typeof.flickpicker.core.DatabaseObject;
import com.typeof.flickpicker.database.DatabaseRecordNotFoundException;
import com.typeof.flickpicker.database.sql.SQLiteDatabaseHelper;

/**
 * SQLDAO
 * Abstract class with methods for creating, reading, updating and deleting
 * database records
 */
abstract class SQLDAO {

    private final SQLiteDatabase db;

    SQLDAO(Context ctx) {
        SQLiteDatabaseHelper mDbHelper = SQLiteDatabaseHelper.getInstance(ctx);
        db = mDbHelper.getWritableDatabase();
    }

    /**
     * Default find method
     *
     * @param id            id to look for
     * @param tableName     which table to look in
     * @return              Cursor object containing record found in database
     * @throws              DatabaseRecordNotFoundException
     */
    Cursor find(long id, String tableName) throws DatabaseRecordNotFoundException {
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE id = ? LIMIT 1", new String[]{String.valueOf(id)});
        if (cursor.getCount() == 0) {
            throw new DatabaseRecordNotFoundException("Record not found in database. UserId: " + id);
        }
        return cursor;
    }

    /**
     * Save entity
     *
     * @param object        database object to save
     * @param tableName     target table
     * @param values        which values to save
     * @return              returns the database rows ID
     */
    long save(DatabaseObject object, String tableName, ContentValues values) {
        // If we have an id on this object
        // Check if it exists in the database

        if (object.getId() != 0) {
            try {
                this.find(object.getId(), tableName);
                update(object, values, tableName);
                return object.getId();
            } catch (DatabaseRecordNotFoundException e) {
                e.printStackTrace();
            }
        }

        long newRowId = db.insert(tableName, " ", values);

        // If there was an error saving the record, we return -1 as the id
        if (newRowId == -1) return -1;

        object.setId(newRowId);

        return newRowId;
    }

    /**
     * Update entity
     *
     * Default update method for SQLDAO
     * @param object        object to update
     * @param values        which values to update
     * @param tableName     which table to target
     */
    void update(DatabaseObject object, ContentValues values, String tableName) {
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
     *
     * @param object        object to save
     * @param tableName     target table
     * @return              returns number of rows deleted
     * @throws              IllegalStateException
     */
    int delete(DatabaseObject object, String tableName) throws IllegalStateException {
        if (object.getId() == 0) {
            throw new IllegalStateException("Core Entity cannot be deleted before it has been saved to the database");
        }

        return db.delete(tableName, "id = " + object.getId(), null);
    }


    /**
     * Default Search method for SQLDAOs
     *
     * @param tableName     which table to search
     * @param column        which column to match
     * @param searchString  which string to search for
     * @return              Cursor from database query
     */
    Cursor search(String tableName, String column, String searchString) {
        return db.rawQuery("SELECT * FROM " + tableName + " WHERE " + column + " LIKE ?",
                new String[]{searchString + "%"});
    }
}
