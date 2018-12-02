package com.leanplum.rondo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.leanplum.rondo.adapters.LeanplumAppAdapter;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;
import com.leanplum.rondo.models.LeanplumEnvironment;

import java.util.ArrayList;

public class LeanplumAppPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_picker);
        final ListView listview = findViewById(R.id.listview);

        LeanplumApp[] apps = new LeanplumApp[] {LeanplumApp.rondoQAProduction()};

        final ArrayList<LeanplumApp> list = new ArrayList<LeanplumApp>();
        for (int i = 0; i < apps.length; ++i) {
            list.add(apps[i]);
        }
        final LeanplumAppAdapter adapter = new LeanplumAppAdapter(this,
                list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final LeanplumApp app = (LeanplumApp) parent.getItemAtPosition(position);
                InternalState.sharedState().setApp(app);
            }

        });

        createButton();
    }

    private void createButton() {
        Button button = findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LeanplumAppPickerActivity.this, AppCreateActivity.class);
                startActivity(myIntent);
            }
        });
    }

}