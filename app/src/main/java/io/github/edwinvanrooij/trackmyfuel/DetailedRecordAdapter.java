package io.github.edwinvanrooij.trackmyfuel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.edwinvanrooij.trackmyfuel.domain.FuelCalculator;
import io.github.edwinvanrooij.trackmyfuel.util.Config;
import io.github.edwinvanrooij.trackmyfuel.util.Preferences;
import me.evrooij.groceries.util.Extensions;

/**
 * Author eddy
 * Created on 5/16/17.
 */

public class DetailedRecordAdapter extends ArrayAdapter<Record> {

    public DetailedRecordAdapter(Context context, ArrayList<Record> items) {
        super(context, 0, items);
        ArrayList<Record> items1 = items;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Record item = getItem(position);

        if (convertView == null) {
            convertView = Extensions.inflate(parent, R.layout.record_row_detailed);
        }

        TextView tvKm = (TextView) convertView.findViewById(R.id.tv_km);
        TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);
        TextView tvTotalCosts = (TextView) convertView.findViewById(R.id.tv_total_costs);

        if (item != null) {
            tvKm.setText(String.format("%s km", item.getKm()));
            tvType.setText(item.getType().toString());

            FuelCalculator calculator = new FuelCalculator(getContext());
            try {
                tvTotalCosts.setText(String.format("%s EUR", calculator.calculate(item.getType(), item.getKm())));
            } catch (Exception e) {
                tvTotalCosts.setText("N.A.");
            }
        }
        return convertView;
    }

}

