package io.github.edwinvanrooij.trackmyfuel;

import android.provider.BaseColumns;

/**
 * Author eddy
 * Created on 5/16/17.
 */

public final class RecordContract {

    public static class Record implements BaseColumns {
        public static final String TABLE_NAME = "record";
        public static final String COLUMN_RECORD_KM = "km";
        public static final String COLUMN_RECORD_TYPE = "type";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Record.TABLE_NAME + " (" +
                        Record._ID + " INTEGER PRIMARY KEY," +
                        Record.COLUMN_RECORD_KM + " INTEGER," +
                        Record.COLUMN_RECORD_TYPE + " INTEGER)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLES IF EXISTS " + Record.TABLE_NAME;
    }

}
