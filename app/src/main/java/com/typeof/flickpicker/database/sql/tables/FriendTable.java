package com.typeof.flickpicker.database.sql.tables;
import android.provider.BaseColumns;

/**
 * FriendTable
 *
 */
public class FriendTable extends SQLTable {

    public FriendTable() {}

    public static abstract class FriendEntry implements BaseColumns {

        private static final String INTEGER_TYPE = " INT";
        private static final String DOUBLE_TYPE = " REAL";
        private static final String COMMA_SEP = ",";
        public static final String TABLE_NAME = "friends";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_USER1ID = "user1id";
        public static final String COLUMN_NAME_USER2ID = "user2id";
        public static final String COLUMN_NAME_DISMATCH = "dismatch";
        public static final String COLUMN_NAME_NUMBER_OF_MOVIES_BOTH_SEEN = "nmbrOfMoviesBothSeen";

        public static String getSQLCreateTableQuery() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_USER1ID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_USER2ID + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_DISMATCH + DOUBLE_TYPE + COMMA_SEP +
                    COLUMN_NAME_NUMBER_OF_MOVIES_BOTH_SEEN + INTEGER_TYPE +
                    " )";
        }

        public static String getSQLDropTableQuery() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }

    }

    @Override
    public String getTableName() {
        return FriendEntry.TABLE_NAME;
    }
}
