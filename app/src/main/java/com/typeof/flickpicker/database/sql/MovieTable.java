package com.typeof.flickpicker.database.sql;

import android.provider.BaseColumns;

/**
 * FlickPicker
 * Group 22
 * Created on 16-04-19.
 */
public class MovieTable extends SQLTable {
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
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_GENRE = "genre";
        public static final String COLUMN_NAME_POSTER = "poster";
        public static final String COLUMN_NAME_VOTES = "votes";
        public static final String COLUMN_NAME_COMMUNITY_RATING = "community_rating";
        public static final String COLUMN_NAME_NULLABLE = " ";

        public static String getSQLCreateTableQuery() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_YEAR + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_GENRE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_POSTER + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_VOTES + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_COMMUNITY_RATING + DOUBLE_TYPE +
            " )";
        }

        public static String getSQLDropTableQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

    @Override
    public String getTableName() {
        return MovieEntry.TABLE_NAME;
    }

}
