package io.github.edwinvanrooij.trackmyfuel;

import android.provider.BaseColumns;

/**
 * Author eddy
 * Created on 5/16/17.
 */

public final class RecordContract {

    public static class Record {
        public static final String TABLE_NAME = "record";
        public static final String COLUMN_RECORD_ID = "id";
        public static final String COLUMN_RECORD_KM = "km";
        public static final String COLUMN_RECORD_TYPE = "type";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Record.TABLE_NAME + " (" +
                        Record.COLUMN_RECORD_ID + " INTEGER PRIMARY KEY," +
                        Record.COLUMN_RECORD_KM + " REAL," +
                        Record.COLUMN_RECORD_TYPE + " INTEGER)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Record.TABLE_NAME;
    }

}
