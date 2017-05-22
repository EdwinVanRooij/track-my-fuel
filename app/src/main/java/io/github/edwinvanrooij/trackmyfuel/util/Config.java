package io.github.edwinvanrooij.trackmyfuel.util;

/**
 * Author: eddy
 * Date: 9-1-17.
 */
public class Config {

    // Parcelable keys
    public static final String KEY_RECORD = "record";

    // Shared preferences keys
    public static final String PREF_KEY_LITER_FOR_KM_INSIDE  = "1";
    public static final String PREF_KEY_LITER_FOR_KM_AVERAGE  = "2";
    public static final String PREF_KEY_LITER_FOR_KM_OUTSIDE  = "3";
    public static final String PREF_KEY_PRICE_FOR_LITER  = "4";

    // Threadpool settings
    public static final int THREADPOOL_MAINACTIVITY_SIZE = 4;
    public static final int THREADPOOL_NEWLIST_SIZE = 4;
}
