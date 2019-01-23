package com.leanplum.rondo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.leanplum.rondo.adapters.LeanplumEnvAdapter;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumEnv;

import java.util.ArrayList;

public class LeanplumEnvPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_picker);
        createButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    private void reloadData() {
        final ListView listview = findViewById(R.id.listview);

        final ArrayList<LeanplumEnv> list = LeanplumEnvPersistence.loadLeanplumEnvs();

        final LeanplumEnvAdapter adapter = new LeanplumEnvAdapter(this,
                list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final LeanplumEnv env = (LeanplumEnv) parent.getItemAtPosition(position);
                RondoPreferences.updateRondoPreferencesWithEnv(env);
                InternalState.sharedState().setEnv(env);
                finish();
            }
        });
    }

    private void createButton() {
        Button button = findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LeanplumEnvPickerActivity.this, EnvCreateActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
