package com.leanplum.rondo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.leanplum.rondo.models.LeanplumEnv;

public class EnvCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_create);

        createButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void createButton() {
        Button button = findViewById(R.id.create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeanplumEnv env = createEnv();
                LeanplumEnvPersistence.saveLeanplumEnv(env);
                finish();
            }
        });
    }

    private void validateInput() {
        EditText displayNameET = findViewById(R.id.displayName);
        String displayName = displayNameET.getText().toString();

        EditText appIdET= findViewById(R.id.appId);
        String appId = appIdET.getText().toString();

        EditText devKeyET = findViewById(R.id.devKey);
        String devKey = devKeyET.getText().toString();

        EditText prodKeyET = findViewById(R.id.prodKey);
        String prodKey = prodKeyET.getText().toString();
    }

    private LeanplumEnv createEnv() {
        EditText apiHostNameET = findViewById(R.id.apiHost);
        String apiHostName = apiHostNameET.getText().toString();

        CheckBox apiSSL = findViewById(R.id.apiSSL);

        EditText socketHostNameET = findViewById(R.id.socketHostName);
        String socketHostName = socketHostNameET.getText().toString();

        EditText socketPortET = findViewById(R.id.socketPort);
        String socketPort = socketPortET.getText().toString();

        LeanplumEnv env = new LeanplumEnv();
        env.setApiHostName(apiHostName);
        env.setApiSSL(apiSSL.isChecked());
        env.setSocketHostName(socketHostName);
        env.setSocketPort(Integer.parseInt(socketPort));
        return env;
    }
}
