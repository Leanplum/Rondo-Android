package com.leanplum.rondo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
}
