package io.github.edwinvanrooij.trackmyfuel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.parceler.Parcels;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_RECORD;

public class ModifyRecordActivity extends AppCompatActivity {

    @BindView(R.id.et_total)
    EditText total;
    @BindView(R.id.spinner)
    Spinner spinner;

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

        initSpinner();
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
        returnIntent.putExtra(KEY_RECORD, Parcels.wrap(mRecord));
        setResult(MainActivity.RESULT_UPDATE, returnIntent);
        finish();
    }
}