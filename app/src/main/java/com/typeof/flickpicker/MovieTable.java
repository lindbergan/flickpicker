package com.typeof.flickpicker;

import android.provider.BaseColumns;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieTable {
    // Prevent instantiating
    public MovieTable() {}

    public static abstract class MovieEntry implements BaseColumns {

        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INT";
        private static final String DOUBLE_TYPE = " REAL";
        private static final String COMMA_SEP = ",";

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_GENRE = "genre";
        public static final String COLUMN_NAME_VOTES = "votes";
        public static final String COLUMN_NAME_COMMUNITY_RATING = "community_rating";
        public static final String COLUMN_NAME_NULLABLE = " NULL";


        public static String getSQLCreateTableQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_GENRE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_VOTES + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_COMMUNITY_RATING + DOUBLE_TYPE +
            " )";
        }

        public static String getSQLDropTableQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

}
