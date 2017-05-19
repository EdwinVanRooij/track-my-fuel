package io.github.edwinvanrooij.trackmyfuel.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.edwinvanrooij.trackmyfuel.DetailedRecordAdapter;
import io.github.edwinvanrooij.trackmyfuel.R;
import io.github.edwinvanrooij.trackmyfuel.Record;
import io.github.edwinvanrooij.trackmyfuel.domain.FuelCalculator;
import io.github.edwinvanrooij.trackmyfuel.persistence.Database;
import io.github.edwinvanrooij.trackmyfuel.util.Config;
import io.github.edwinvanrooij.trackmyfuel.util.Preferences;
import me.evrooij.groceries.util.Extensions;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillFragment extends BaseFragment {


    @BindView(R.id.tvTotalDistance)
    TextView mTotalDistance;

    @BindView(R.id.tvTotalCosts)
    TextView mTotalCosts;

    @BindView(R.id.listview)
    ListView mListview;

    DetailedRecordAdapter mAdapter;

    Database db;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListView();

        db = new Database(getContext());

        List<Record> recordList = db.getAllRecords();
        mAdapter.addAll(recordList);

        double totalDistance = 0;
        double totalPrice = 0;

        FuelCalculator calculator = new FuelCalculator(getContext());
        for (Record r : recordList) {
            try {
                totalPrice += calculator.calculate(r.getType(), r.getKm());
            } catch (Exception e) {
                e.printStackTrace();
            }
            totalDistance += r.getKm();
        }

        mTotalDistance.setText(String.format("(%s km)", (double) Math.round(totalDistance * 100) / 100));
        mTotalCosts.setText(String.format("(%s EUR)", (double) Math.round(totalPrice * 100) / 100));
    }

    public void initListView() {
        ArrayList<Record> data = new ArrayList<>();
        mAdapter = new DetailedRecordAdapter(getContext(), data);

        mListview.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return Extensions.inflate(container, R.layout.fragment_bill);

    }
}
