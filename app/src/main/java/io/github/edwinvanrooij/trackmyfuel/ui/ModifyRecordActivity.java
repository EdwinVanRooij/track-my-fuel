package io.github.edwinvanrooij.trackmyfuel.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.edwinvanrooij.trackmyfuel.R;
import io.github.edwinvanrooij.trackmyfuel.Record;

import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_RECORD;

public class ModifyRecordActivity extends AppCompatActivity {

    @BindView(R.id.et_total)
    EditText mTotal;
    @BindView(R.id.spinner)
    Spinner mSpinner;

    @BindString(R.string.inside)
    String inside;
    @BindString(R.string.average)
    String average;
    @BindString(R.string.outside)
    String outside;

    Record mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_record);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecord = Parcels.unwrap(getIntent().getParcelableExtra(KEY_RECORD));
        mTotal.setText(String.valueOf(mRecord.getKm()));

        initSpinner();

        if (Objects.equals(mRecord.getType().toString(),  inside.toUpperCase())) {
            mSpinner.setSelection(0);
        } else if (Objects.equals(mRecord.getType().toString(), average.toUpperCase())) {
            mSpinner.setSelection(1);
        } else if (Objects.equals(mRecord.getType().toString(), outside.toUpperCase())) {
            mSpinner.setSelection(2);
        } else {
            Toast.makeText(this,
                    "Could not determine spinner selection, it equals none of the accepted types: %s does not equal (eg) %s"
                    , Toast.LENGTH_SHORT).show();
        }
    }

    private void initSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the mAdapter to the spinner
        mSpinner.setAdapter(adapter);
    }

    @OnClick(R.id.btn_delete)
    void delete() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_RECORD, Parcels.wrap(mRecord));
        setResult(MainActivity.RESULT_DELETE, returnIntent);
        finish();
    }

    @OnClick(R.id.btn_update)
    void update() {
        Intent returnIntent = new Intent();

        mRecord.setKm(Double.valueOf(mTotal.getText().toString()));

        String spinnerSelection = mSpinner.getSelectedItem().toString();
        if (Objects.equals(spinnerSelection, inside)) {
            mRecord.setType( Record.Type.INSIDE);
        } else if (Objects.equals(spinnerSelection, average)) {
            mRecord.setType( Record.Type.AVERAGE);
        } else if (Objects.equals(spinnerSelection, outside)) {
            mRecord.setType( Record.Type.OUTSIDE);
        } else {
            Toast.makeText(this,
                    "Could not determine spinner selection, it equals none of the accepted types: {}"
                    , Toast.LENGTH_SHORT).show();
        }

        returnIntent.putExtra(KEY_RECORD, Parcels.wrap(mRecord));
        setResult(MainActivity.RESULT_UPDATE, returnIntent);
        finish();
    }
}
