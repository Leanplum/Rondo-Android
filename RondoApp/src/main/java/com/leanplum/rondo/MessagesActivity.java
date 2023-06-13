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

import com.clevertap.android.sdk.leanplum.LeanplumCT;
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
                    LeanplumCT.track(item);
                }
            }

        });
    }

    private void logImpressionOccurrences() {

    }

    private void logTriggerOccurrences() {

    }
}
