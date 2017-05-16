package io.github.edwinvanrooij.trackmyfuel;

import android.provider.BaseColumns;

/**
 * Created by eddy on 5/16/17.
 */

public final class RecordContract {

    public static class Record implements BaseColumns {
        public static final String TABLE_NAME = "record";
        public static final String COLUMN_RECORD_KM = "km";
        public static final String COLUMN_RECORD_TYPE = "type";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Record.TABLE_NAME + " (" +
                        Record._ID + " INTEGER PRIMARY KEY, "
//                https://developer.android.com/training/basics/data-storage/databases.html
    }






}
