package com.typeof.flickpicker.database;
import android.provider.BaseColumns;

public class RatingTable {
    // Prevent instantiating
    public RatingTable() {}

    public static abstract class RatingEntry implements BaseColumns {

        private static final String INTEGER_TYPE = " INT";
        private static final String DOUBLE_TYPE = " REAL";
        private static final String COMMA_SEP = ",";

        public static final String TABLE_NAME = "ratings";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_MOVIEID = "movieId";
        public static final String COLUMN_NAME_USERID = "userId";
        public static final String COLUMN_NAME_NULLABLE = " NULL";


        public static String getSQLCreateTableQuery() {
            return "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_RATING + DOUBLE_TYPE + COMMA_SEP +
                    COLUMN_NAME_MOVIEID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_USERID + INTEGER_TYPE +
                    " )";
        }

        public static String getSQLDropTableQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

}

