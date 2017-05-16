package io.github.edwinvanrooij.trackmyfuel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;

import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_RECORD;

public class MainActivity extends AppCompatActivity {

    public final static int MODIFY_RECORD = 1;

    public final static int RESULT_UPDATE = 100;
    public final static int RESULT_DELETE = 101;

    @BindView(R.id.et_total)
    EditText total;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.listview)
    ListView listview;

    @BindString(R.string.inside)
    String inside;
    @BindString(R.string.average)
    String average;
    @BindString(R.string.outside)
    String outside;

    RecordAdapter mAdapter;
    RecordDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initSpinner();
        initListView();
        mAdapter.addAll(getAllRecords());
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

    private void initListView() {
        ArrayList<Record> data = new ArrayList<>();
        mAdapter = new RecordAdapter(this, data);

        listview.setAdapter(mAdapter);
    }

    @OnItemLongClick(R.id.listview)
    boolean onItemLongClick(int position) {
        Record r = mAdapter.getItem(position);
        Toast.makeText(this, String.format("Clicked %s", r), Toast.LENGTH_SHORT).show();

        startActivityForResult(new Intent(this, ModifyRecordActivity.class).putExtra(KEY_RECORD, Parcels.wrap(r)), MODIFY_RECORD);
        return true;
    }

    @OnClick(R.id.btn_add)
    void add() {
        int km = Integer.valueOf(total.getText().toString());

        Record.Type type = null;

        String spinnerSelection = spinner.getSelectedItem().toString();

        if (Objects.equals(spinnerSelection, inside)) {
            type = Record.Type.INSIDE;
        } else if (Objects.equals(spinnerSelection, average)) {
            type = Record.Type.AVERAGE;
        } else if (Objects.equals(spinnerSelection, outside)) {
            type = Record.Type.OUTSIDE;
        } else {
            Toast.makeText(this,
                    "Could not determine spinner selection, it equals none of the accepted types: {}"
                    , Toast.LENGTH_SHORT).show();
        }

        Record record = new Record(km, type);

        mAdapter.add(record);
        addToDb(record);
    }
//
//    private void updateDb(Record record) {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        // New value for one column
//        ContentValues values = new ContentValues();
//        values.put(RecordContract.Record.COLUMN_RECORD_KM, record.getKm());
//        values.put(RecordContract.Record.COLUMN_RECORD_TYPE, record.getType().ordinal());
//
//        // Which row to update, based on the title
//        String selection = RecordContract.Record.COLUMN_RECORD_ID + " = ?";
//
//        int count = db.update(
//                RecordContract.Record.TABLE_NAME,
//                values,
//                selection);
//
//        long newRowId = db.update(RecordContract.Record.TABLE_NAME, values);
//    }
    private void addToDb(Record record) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(RecordContract.Record.COLUMN_RECORD_KM, record.getKm());
        values.put(RecordContract.Record.COLUMN_RECORD_TYPE, record.getType().ordinal());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(RecordContract.Record.TABLE_NAME, null, values);
    }

    private void initSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the mAdapter to the spinner
        spinner.setAdapter(adapter);
    }

    private List<Record> getAllRecords() {
        List<Record> resultList = new ArrayList<>();

        mDbHelper = new RecordDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(String.format("select * from %s", RecordContract.Record.TABLE_NAME), null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Integer km = cursor.getInt(cursor.getColumnIndex(RecordContract.Record.COLUMN_RECORD_KM));
                Integer type = cursor.getInt(cursor.getColumnIndex(RecordContract.Record.COLUMN_RECORD_TYPE));

                Record record = new Record(km, Record.Type.values()[type]);
                resultList.add(record);

                cursor.moveToNext();
            }
        }
        cursor.close();

        return resultList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MODIFY_RECORD) {
            if (resultCode == RESULT_UPDATE) {
                Record record = Parcels.unwrap(data.getParcelableExtra(KEY_RECORD));
                Toast.makeText(this, String.format("Update: %s", record.toString()), Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_DELETE) {
                Record record = Parcels.unwrap(data.getParcelableExtra(KEY_RECORD));
                Toast.makeText(this, String.format("Delete: %s", record.toString()), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
