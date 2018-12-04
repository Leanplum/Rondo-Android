package com.leanplum.rondo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AdhocPersistence {

    static String ADHOC_PERSISTENCE_EVENT_KEY = "ADHOC_PERSISTENCE_EVENT_KEY";
    static String ADHOC_PERSISTENCE_STATE_KEY = "ADHOC_PERSISTENCE_STATE_KEY";

    private Context context;


    public AdhocPersistence(Context context) {
        this.context = context;
    }

    public String loadSavedEvent() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(ADHOC_PERSISTENCE_EVENT_KEY, "");
    }

    public String loadSavedState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(ADHOC_PERSISTENCE_STATE_KEY, "");

    }

    public void saveEvent(String event) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(ADHOC_PERSISTENCE_EVENT_KEY, event).apply();
    }

    public void saveState(String state) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putString(ADHOC_PERSISTENCE_STATE_KEY, state).apply();
    }
}
