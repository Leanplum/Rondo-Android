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

import java.util.ArrayList;

public class LeanplumAppPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_picker);
        createButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    private void reloadData() {
        final ListView listview = findViewById(R.id.listview);

        final ArrayList<LeanplumApp> list = LeanplumAppPersistence.loadLeanplumApps();

        final LeanplumAppAdapter adapter = new LeanplumAppAdapter(this,
                list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final LeanplumApp app = (LeanplumApp) parent.getItemAtPosition(position);
                InternalState.sharedState().setApp(app);
                RondoPreferences.updateRondoPreferencesWithApp(app);
                finish();
            }

        });
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
