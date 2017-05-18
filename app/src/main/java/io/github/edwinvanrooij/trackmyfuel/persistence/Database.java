package io.github.edwinvanrooij.trackmyfuel.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.github.edwinvanrooij.trackmyfuel.Record;
import io.github.edwinvanrooij.trackmyfuel.RecordContract;
import io.github.edwinvanrooij.trackmyfuel.RecordDbHelper;

/**
 * Author eddy
 * Created on 5/18/17.
 */

public class Database {

    RecordDbHelper mDbHelper;

    public Database(Context context) {
        mDbHelper = new RecordDbHelper(context);
    }

    public boolean deleteFromDb(Record record) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        return db.delete(RecordContract.Record.TABLE_NAME, RecordContract.Record.COLUMN_RECORD_ID + "=" + record.getId(), null) > 0;
    }

    public void updateFromDb(Record record) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RecordContract.Record.COLUMN_RECORD_KM, record.getKm());
        cv.put(RecordContract.Record.COLUMN_RECORD_TYPE, record.getType().ordinal());

        db.update(RecordContract.Record.TABLE_NAME, cv, RecordContract.Record.COLUMN_RECORD_ID + "=" + record.getId(), null);
    }

    public Record addToDb(Record record) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RecordContract.Record.COLUMN_RECORD_KM, record.getKm());
        values.put(RecordContract.Record.COLUMN_RECORD_TYPE, record.getType().ordinal());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(RecordContract.Record.TABLE_NAME, null, values);

        Record newRecord = new Record((int) newRowId, record.getKm(), record.getType());

        System.out.println(String.format("Returning new record; %s", newRecord));
        mDbHelper.close();

        return newRecord;
    }
    public List<Record> getAllRecords() {
        List<Record> resultList = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(String.format("select * from %s", RecordContract.Record.TABLE_NAME), null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Integer id = cursor.getInt(cursor.getColumnIndex(RecordContract.Record.COLUMN_RECORD_ID));
                Integer km = cursor.getInt(cursor.getColumnIndex(RecordContract.Record.COLUMN_RECORD_KM));
                Integer type = cursor.getInt(cursor.getColumnIndex(RecordContract.Record.COLUMN_RECORD_TYPE));

                Record record = new Record(id, km, Record.Type.values()[type]);
                resultList.add(record);

                cursor.moveToNext();
            }
        }
        cursor.close();

        return resultList;
    }
}
