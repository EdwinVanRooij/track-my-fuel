package io.github.edwinvanrooij.trackmyfuel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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

    RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initSpinner();
        initListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initListView() {
        ArrayList<Record> data = new ArrayList<>();
        adapter = new RecordAdapter(this, data);

        listview.setAdapter(adapter);
    }

    @OnClick(R.id.btn_add)
    void add() {
        int km = Integer.valueOf(total.getText().toString());

        Record.Type type = null;

        String spinnerSelection = spinner.getSelectedItem().toString();

        if (Objects.equals(spinnerSelection, inside)) {
            type = Record.Type.Inside;
        } else if (Objects.equals(spinnerSelection, average)) {
            type = Record.Type.Average;
        } else if (Objects.equals(spinnerSelection, outside)) {
            type = Record.Type.Outside;
        } else {
            Toast.makeText(this,
                    "Could not determine spinner selection, it equals none of the accepted types: {}"
                    , Toast.LENGTH_SHORT).show();
        }

        Record record = new Record(km, type);

        adapter.add(record);
    }

    private void initSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
}
