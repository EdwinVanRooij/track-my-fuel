package io.github.edwinvanrooij.trackmyfuel.ui;


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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.edwinvanrooij.trackmyfuel.DetailedRecordAdapter;
import io.github.edwinvanrooij.trackmyfuel.R;
import io.github.edwinvanrooij.trackmyfuel.Record;
import io.github.edwinvanrooij.trackmyfuel.persistence.Database;
import io.github.edwinvanrooij.trackmyfuel.util.Config;
import io.github.edwinvanrooij.trackmyfuel.util.Preferences;
import me.evrooij.groceries.util.Extensions;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment {


    @BindView(R.id.et_liter_for_km_inside)
    EditText mLitersInside;
    @BindView(R.id.et_liter_for_km_average)
    EditText mLitersAverage;
    @BindView(R.id.et_liter_for_km_outside)
    EditText mLitersOutside;
    @BindView(R.id.et_price_for_liter)
    EditText mPrice;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Object inside = Preferences.getValue(getContext(), Config.PREF_KEY_LITER_FOR_KM_INSIDE);
        Object average = Preferences.getValue(getContext(), Config.PREF_KEY_LITER_FOR_KM_AVERAGE);
        Object outside = Preferences.getValue(getContext(), Config.PREF_KEY_LITER_FOR_KM_OUTSIDE);
        Object price = Preferences.getValue(getContext(), Config.PREF_KEY_PRICE_FOR_LITER);

        if (inside != null) {
            mLitersInside.setText(String.valueOf(inside));
        }
        if (average != null) {
            mLitersAverage.setText(String.valueOf(average));
        }
        if (outside != null) {
            mLitersOutside.setText(String.valueOf(outside));
        }
        if (price != null) {
            mPrice.setText(String.valueOf(price));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return Extensions.inflate(container, R.layout.fragment_settings);
    }

    @OnClick(R.id.btn_save)
    void save() {
        double inside = Double.valueOf(mLitersInside.getText().toString());
        double average = Double.valueOf(mLitersAverage.getText().toString());
        double outside = Double.valueOf(mLitersOutside.getText().toString());
        double price = Double.valueOf(mPrice.getText().toString());

        Preferences.setValue(getContext(), Config.PREF_KEY_LITER_FOR_KM_INSIDE, inside);
        Preferences.setValue(getContext(), Config.PREF_KEY_LITER_FOR_KM_AVERAGE, average);
        Preferences.setValue(getContext(), Config.PREF_KEY_LITER_FOR_KM_OUTSIDE, outside);
        Preferences.setValue(getContext(), Config.PREF_KEY_PRICE_FOR_LITER, price);
    }
}
