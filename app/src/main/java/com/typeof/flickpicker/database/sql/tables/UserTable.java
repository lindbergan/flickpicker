package com.typeof.flickpicker.database.sql.tables;

import android.provider.BaseColumns;

import com.typeof.flickpicker.database.sql.tables.SQLTable;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-20.
 */

public class UserTable extends SQLTable {

    public static abstract class UserEntry implements BaseColumns {

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INT";
        private static final String COMMA_SEP = ",";
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_SCORE = "score";


        public static String getSQLCreateTableQuery() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_USERNAME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_SCORE + INTEGER_TYPE +
                    " )";
        }

        public static String getSQLDropTableQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

    @Override
    public String getTableName() {
        return UserEntry.TABLE_NAME;
    }
}
