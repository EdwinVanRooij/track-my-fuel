package io.github.edwinvanrooij.trackmyfuel.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.trackmyfuel.DetailedRecordAdapter;
import io.github.edwinvanrooij.trackmyfuel.Payer;
import io.github.edwinvanrooij.trackmyfuel.PayerAdapter;
import io.github.edwinvanrooij.trackmyfuel.R;
import io.github.edwinvanrooij.trackmyfuel.Record;
import io.github.edwinvanrooij.trackmyfuel.domain.FuelCalculator;
import io.github.edwinvanrooij.trackmyfuel.persistence.Database;
import me.evrooij.groceries.util.Extensions;

import static io.github.edwinvanrooij.trackmyfuel.util.Config.KEY_PAYER;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends BaseFragment {


    @BindView(R.id.total)
    EditText mTotal;

    @BindView(R.id.listview)
    ListView mListview;

    private double mPaidAmount = 0;

    PayerAdapter mAdapter;
    List<Payer> mPayers = new ArrayList<>();

    Database db;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListView();
        double totalDistance = 0;
        double totalPrice = 0;

        FuelCalculator calculator = new FuelCalculator(getContext());

        db = new Database(getContext());
        List<Record> recordList = db.getAllRecords();
        for (Record r : recordList) {
            try {
                totalPrice += calculator.calculateRecord(r.getType(), r.getKm());
            } catch (Exception e) {
                e.printStackTrace();
            }
            totalDistance += r.getKm();
        }
        double total_costs = (double) Math.round(totalPrice * 100) / 100;

        Payer me = new Payer(getString(R.string.me), total_costs, 0);

        mAdapter.add(me);
        mPayers.add(me);
    }

    @OnClick(R.id.btn_ok)
    void ok() {
        mPaidAmount = Double.parseDouble(mTotal.getText().toString());
        recalculateExtra();
    }

    @OnClick(R.id.btn_add)
    void add() {
        mainActivity.startActivityForResult(null);
    }

    @Override
    public void onContainterActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.ADD_PAYER:
                switch (resultCode) {
                    case MainActivity.RESULT_ADD: {
                        Payer payer = Parcels.unwrap(data.getParcelableExtra(KEY_PAYER));
                        mPayers.add(payer);
                        Toast.makeText(mainActivity, String.format("Adding payer %s", payer.toString()), Toast.LENGTH_SHORT).show();
                        refreshListview();
                        break;
                    }
                }
                break;
        }
    }

    private void recalculateExtra() {
        int amountOfPayers = mAdapter.getCount();

        double totalOwnCosts = 0;

        for (Payer p : mPayers) {
            totalOwnCosts += p.getOwnCosts();
        }


        // If there was paid more than was necessary, charge payers additionally
        double toBePaid = mPaidAmount - totalOwnCosts;

        double extraForEachPayer = toBePaid / amountOfPayers;

        for (Payer p : mPayers) {
            p.setExtra(extraForEachPayer);
        }
        refreshListview();
    }

    public void refreshListview() {
        mAdapter.clear();
        mAdapter.addAll(mPayers);
    }

    public void initListView() {
        ArrayList<Payer> data = new ArrayList<>();
        mAdapter = new PayerAdapter(getContext(), data);

        mListview.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return Extensions.inflate(container, R.layout.fragment_payment);

    }
}
