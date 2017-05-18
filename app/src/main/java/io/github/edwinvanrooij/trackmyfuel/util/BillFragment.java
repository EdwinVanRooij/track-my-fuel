package io.github.edwinvanrooij.trackmyfuel.util;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import io.github.edwinvanrooij.trackmyfuel.DetailedRecordAdapter;
import io.github.edwinvanrooij.trackmyfuel.R;
import io.github.edwinvanrooij.trackmyfuel.Record;
import io.github.edwinvanrooij.trackmyfuel.persistence.Database;
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
        mAdapter.addAll(db.getAllRecords());

        // TODO: 5/18/17 calculate total amount of km from db
        mTotalDistance.setText("30 km");

        // TODO: 5/18/17 calculate total price
        mTotalDistance.setText("53 EUR");
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
