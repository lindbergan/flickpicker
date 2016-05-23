package com.typeof.flickpicker.database.sql.tables;

import android.provider.BaseColumns;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-25.
 */

public class PlaylistTable extends SQLTable {

    // Prevent instantiating
    public PlaylistTable() {}

    public static abstract class PlaylistEntry implements BaseColumns {

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INT";
        private static final String COMMA_SEP = ",";
        public static final String TABLE_NAME = "playlists";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_MOVIES_LIST = "movies_list";

        public static String getSQLCreateTableQuery() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_USER_ID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_MOVIES_LIST + TEXT_TYPE +
                    " )";
        }

        public static String getSQLDropTableQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

    @Override
    public String getTableName() {
        return PlaylistEntry.TABLE_NAME;
    }
}
