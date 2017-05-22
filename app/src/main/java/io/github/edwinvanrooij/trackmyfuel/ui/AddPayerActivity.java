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
import io.github.edwinvanrooij.trackmyfuel.Payer;
import io.github.edwinvanrooij.trackmyfuel.R;
import io.github.edwinvanrooij.trackmyfuel.Record;

import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_PAYER;
import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_RECORD;

public class AddPayerActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText mName;
    @BindView(R.id.et_paid)
    EditText mPaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payer);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.btn_add)
    void add() {
        Intent returnIntent = new Intent();

        String name = mName.getText().toString();
        double owncosts = Double.parseDouble(mPaid.getText().toString());

        Payer p = new Payer(name, owncosts, 0);

        returnIntent.putExtra(KEY_PAYER, Parcels.wrap(p));
        setResult(MainActivity.RESULT_ADD, returnIntent);
        finish();
    }
}
