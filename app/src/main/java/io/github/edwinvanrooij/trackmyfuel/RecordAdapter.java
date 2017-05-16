package io.github.edwinvanrooij.trackmyfuel;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import me.evrooij.groceries.util.Extensions;

/**
 * Created by eddy on 5/16/17.
 */

public class RecordAdapter extends ArrayAdapter<Record> {

    private final ArrayList<Record> items;

    public RecordAdapter(Context context, ArrayList<Record> items) {
        super(context, 0, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Record item = getItem(position);

        if (convertView == null) {
            convertView = Extensions.inflate(parent, R.layout.record_row);
        }

        TextView tvKm = (TextView) convertView.findViewById(R.id.tv_km);
        TextView tvType = (TextView) convertView.findViewById(R.id.tv_type);

        if (item != null) {
            tvKm.setText(String.format("%s km", item.getKm()));
            tvType.setText(item.getType().toString());
        }

        return convertView;
    }

    public ArrayList<Record> getItems() {
        return items;
    }
}

