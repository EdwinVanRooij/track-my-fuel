package io.github.edwinvanrooij.trackmyfuel.domain;

import android.content.Context;

import io.github.edwinvanrooij.trackmyfuel.Record;
import io.github.edwinvanrooij.trackmyfuel.util.Config;
import io.github.edwinvanrooij.trackmyfuel.util.Preferences;

import static io.github.edwinvanrooij.trackmyfuel.Record.Type.INSIDE;

/**
 * Author eddy
 * Created on 5/19/17.
 */

public class FuelCalculator {

    private Context context;

    public FuelCalculator(Context context) {
        this.context = context;
    }

    public double calculate(Record.Type type, double km) throws Exception {
        switch (type) {
            case INSIDE:
                return calculate(km, (double) Preferences.getValue(context, Config.PREF_KEY_LITER_FOR_KM_INSIDE), (double) Preferences.getValue(context, Config.PREF_KEY_PRICE_FOR_LITER));
            case AVERAGE:
                return calculate(km, (double) Preferences.getValue(context, Config.PREF_KEY_LITER_FOR_KM_AVERAGE), (double) Preferences.getValue(context, Config.PREF_KEY_PRICE_FOR_LITER));
            case OUTSIDE:
                return calculate(km, (double) Preferences.getValue(context, Config.PREF_KEY_LITER_FOR_KM_OUTSIDE), (double) Preferences.getValue(context, Config.PREF_KEY_PRICE_FOR_LITER));
            default:
                throw new Exception("No type found.");
        }
    }

    private double calculate(double km, double factor, double literPrice) {
        double costs =  factor * km * literPrice;
        return (double) Math.round(costs * 100) / 100;
    }
}
