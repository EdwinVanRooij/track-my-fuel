package io.github.edwinvanrooij.trackmyfuel.util;

import android.content.Context;

import com.orhanobut.hawk.Hawk;

/**
 * Author: eddy
 * Date: 14-1-17.
 */
public class Preferences {

    public static void setValue(Context context, String key, Object value) {
        Hawk.init(context).build();
        Hawk.put(key, value);
    }
    public static Object getValue(Context context, String key) {
        Hawk.init(context).build();
        if (Hawk.contains(key)) {
            return Hawk.get(key);
        }
        return null;
    }

    public static void removeAll(Context context) {
        Hawk.init(context).build();
        Hawk.deleteAll();
    }
}
