package io.github.edwinvanrooij.trackmyfuel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.evrooij.groceries.util.Extensions;

/**
 * Author eddy
 * Created on 5/16/17.
 */

public class PayerAdapter extends ArrayAdapter<Payer> {

    public PayerAdapter(Context context, ArrayList<Payer> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Payer item = getItem(position);

        if (convertView == null) {
            convertView = Extensions.inflate(parent, R.layout.payer_row);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvOwnCosts = (TextView) convertView.findViewById(R.id.tv_own_costs);
        TextView tvExtra = (TextView) convertView.findViewById(R.id.tv_extra);
        TextView tvTotalCosts = (TextView) convertView.findViewById(R.id.tv_total);

        if (item != null) {
            tvName.setText(item.getName());
            tvOwnCosts.setText(String.valueOf((double) Math.round(item.getOwnCosts() * 100) / 100));
            tvExtra.setText(String.valueOf((double) Math.round(item.getExtra() * 100) / 100));
            tvTotalCosts.setText(String.valueOf((double) Math.round((item.getExtra() + item.getOwnCosts()) * 100) / 100));
        }

        return convertView;
    }

}

