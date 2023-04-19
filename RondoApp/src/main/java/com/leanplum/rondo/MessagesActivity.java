package com.leanplum.rondo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.leanplum.Leanplum;

import com.leanplum.internal.Constants;
import com.leanplum.internal.Log;
import java.util.ArrayList;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    private static final String LOG_IMPRESSIONS = "Log impression occurrences";
    private static final String LOG_TRIGGERS = "Log trigger occurrences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        final ListView listview = findViewById(R.id.listview);
        String[] values = new String[] { "alert", "centerPopup", "confirm",
                "interstitial", "richInterstitial", "imageInterstitial", "webInterstitial", "banner",
                LOG_IMPRESSIONS, LOG_TRIGGERS};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                if (LOG_IMPRESSIONS.equals(item)) {
                    logImpressionOccurrences();
                } else if (LOG_TRIGGERS.equals(item)) {
                    logTriggerOccurrences();
                } else {
                    Leanplum.track(item);
                }
            }

        });
    }

    private void logImpressionOccurrences() {
        String prefix = String.format(Constants.Defaults.MESSAGE_IMPRESSION_OCCURRENCES_KEY, "");

        Context context = Leanplum.getContext();
        SharedPreferences prefs =
            context.getSharedPreferences("__leanplum_messaging__", Context.MODE_PRIVATE);
        Map<String, ?> all = prefs.getAll();

        boolean hasImpressions = false;

        for (Map.Entry<String, ?> entry : all.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                String json = (String) entry.getValue();
                if (!TextUtils.isEmpty(json)) {
                    hasImpressions = true;
                    Log.d("messageId=" + entry.getKey().replace(prefix, "") + " -> " + json);
                }
            }
        }

        if (!hasImpressions) {
            Log.d("No impression occurrences yet.");
        }
    }

    private void logTriggerOccurrences() {
        String prefix = String.format(Constants.Defaults.MESSAGE_TRIGGER_OCCURRENCES_KEY, "");

        Context context = Leanplum.getContext();
        SharedPreferences prefs =
            context.getSharedPreferences("__leanplum_messaging__", Context.MODE_PRIVATE);
        Map<String, ?> all = prefs.getAll();

        boolean hasTriggers = false;

        for (Map.Entry<String, ?> entry : all.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                Object value = entry.getValue();
                if (value != null) {
                    hasTriggers = true;
                    Log.d("messageId=" + entry.getKey().replace(prefix, "") + " -> " + value);
                }
            }
        }

        if (!hasTriggers) {
            Log.d("No trigger occurrences yet.");
        }
    }
}
