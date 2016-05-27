package com.typeof.flickpicker.database.sql.tables;
import android.database.sqlite.SQLiteDatabase;

/**
 * FlickPicker
 * Group 22
 * Created on 16-05-21.
 */

public abstract class SQLTable {

    public String hasBeenCreatedSQLQuery() {
        return "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + getTableName() + "'";
    }

    protected abstract String getTableName();
}
