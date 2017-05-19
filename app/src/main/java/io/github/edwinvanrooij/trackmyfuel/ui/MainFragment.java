package io.github.edwinvanrooij.trackmyfuel.ui;

import android.content.Intent;
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
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import io.github.edwinvanrooij.trackmyfuel.R;
import io.github.edwinvanrooij.trackmyfuel.Record;
import io.github.edwinvanrooij.trackmyfuel.RecordAdapter;
import io.github.edwinvanrooij.trackmyfuel.persistence.Database;
import me.evrooij.groceries.util.Extensions;

import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_RECORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

    RecordAdapter mAdapter;

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

    Database db;

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

        db = new Database(getContext());
        mAdapter.addAll(db.getAllRecords());
    }

    @Override
    public void onContainterActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.MODIFY_RECORD:
                switch (resultCode) {
                    case MainActivity.RESULT_UPDATE: {
                        Record record = Parcels.unwrap(data.getParcelableExtra(KEY_RECORD));
                        db.updateFromDb(record);
                        refreshListview();
                        break;
                    }
                    case MainActivity.RESULT_DELETE: {
                        Record record = Parcels.unwrap(data.getParcelableExtra(KEY_RECORD));
                        db.deleteFromDb(record);
                        refreshListview();
                        break;
                    }
                }
                break;
        }
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

        mainActivity.startActivityForResult(r);
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

        record = db.addToDb(record);
        mAdapter.add(record);
    }

    public void refreshListview() {
        mAdapter.clear();
        mAdapter.addAll(db.getAllRecords());
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

}
