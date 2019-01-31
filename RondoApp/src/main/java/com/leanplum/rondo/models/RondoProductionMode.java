package com.leanplum.rondo.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RondoProductionMode {
    static private final String RONDO_PRODUCTION_MODE_KEY = "RONDO_PRODUCTION_MODE_KEY";

    static public boolean isProductionMode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(RONDO_PRODUCTION_MODE_KEY, false);
    }

    static public void setProductionMode(Context context, boolean productionMode) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().
                putBoolean(RONDO_PRODUCTION_MODE_KEY, productionMode).apply();
    }
}
