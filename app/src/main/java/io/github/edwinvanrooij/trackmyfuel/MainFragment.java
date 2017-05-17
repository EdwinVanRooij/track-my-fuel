package io.github.edwinvanrooij.trackmyfuel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import me.evrooij.groceries.util.Extensions;

import static io.github.edwinvanrooij.trackmyfuel.MainActivity.MODIFY_RECORD;
import static io.github.edwinvanrooij.trackmyfuel.MainActivity.RESULT_DELETE;
import static io.github.edwinvanrooij.trackmyfuel.MainActivity.RESULT_UPDATE;
import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_RECORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

//    @BindView(R.id.lv_my_groceries)
//    ListView listView;
//
//    public ProductAdapter adapter;
//    public Product editingProduct;

    RecordAdapter mAdapter;
    RecordDbHelper mDbHelper;

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

    public final static int NEW_PRODUCT_CODE = 1;
    public final static int EDIT_PRODUCT_CODE = 2;
    public static final String TAG = "DefaultListFragment";

//    public GroceryList getContext()List;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return Extensions.inflate(container, R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSpinner();
        initListView();
        mAdapter.addAll(getAllRecords());
    }

    public void initListView() {
        ArrayList<Record> data = new ArrayList<>();
        mAdapter = new RecordAdapter(getContext(), data);

        listview.setAdapter(mAdapter);
    }

    @OnItemLongClick(R.id.listview)
    boolean onItemLongClick(int position) {
        Record r = mAdapter.getItem(position);
        Toast.makeText(getContext(), String.format("Clicked %s", r), Toast.LENGTH_SHORT).show();

        startActivityForResult(new Intent(getContext(), ModifyRecordActivity.class).putExtra(KEY_RECORD, Parcels.wrap(r)), MODIFY_RECORD);
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
            Toast.makeText(getContext(),
                    "Could not determine spinner selection, it equals none of the accepted types: {}"
                    , Toast.LENGTH_SHORT).show();
        }

        Record record = new Record(km, type);

        record = addToDb(record);
        mAdapter.add(record);
    }

    public void refreshListview() {
        mAdapter.clear();
        mAdapter.addAll(getAllRecords());
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

    public void initSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the mAdapter to the spinner
        spinner.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case MODIFY_RECORD:

                switch (resultCode) {
                    case RESULT_UPDATE: {

                        Record record = Parcels.unwrap(data.getParcelableExtra(KEY_RECORD));

                        updateFromDb(record);
                        refreshListview();

                        Toast.makeText(getContext(), String.format("Updated: %s", record.toString()), Toast.LENGTH_SHORT).show();

                        break;
                    }
                    case RESULT_DELETE: {

                        Record record = Parcels.unwrap(data.getParcelableExtra(KEY_RECORD));

                        deleteFromDb(record);
                        refreshListview();

                        Toast.makeText(getContext(), String.format("Deleted: %s", record.toString()), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        Toast.makeText(getContext(), String.format("Resultcode was %s, looking for %s", resultCode, RESULT_UPDATE), Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                Toast.makeText(getContext(), String.format("Requestcode was %s, looking for %s", requestCode, MODIFY_RECORD), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public List<Record> getAllRecords() {
        List<Record> resultList = new ArrayList<>();

        mDbHelper = new RecordDbHelper(getContext());
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
