package com.leanplum.rondo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leanplum.Leanplum;
import com.leanplum.annotations.Parser;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;
import com.leanplum.rondo.models.LeanplumEnv;

public class AppSetupActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_app_setup, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        createStartButton();
        createAppPickerButton();
        createEnvPickerButton();
    }


    @Override
    public void onResume() {
        super.onResume();
        populateAppDetails();
    }

    private void createAppPickerButton() {
        Button button = getView().findViewById(R.id.app_picker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), LeanplumAppPickerActivity.class);
                startActivity(myIntent);
            }
        });
    }
    private void createEnvPickerButton() {
        Button button = getView().findViewById(R.id.env_picker);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), LeanplumEnvPickerActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void createStartButton() {
        Button button = getView().findViewById(R.id.call_start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLeanplum();
            }
        });
    }


    private void populateAppDetails() {
        InternalState state = InternalState.sharedState();
        LeanplumApp app = state.getApp();
        LeanplumEnv env = state.getEnv();

        ((TextView)getView().findViewById(R.id.appName)).setText(app.getDisplayName());
        ((TextView)getView().findViewById(R.id.appId)).setText(app.getAppId());
        ((TextView)getView().findViewById(R.id.devKey)).setText(app.getDevKey());
        ((TextView)getView().findViewById(R.id.prodKey)).setText(app.getProdKey());

        ((TextView)getView().findViewById(R.id.userId)).setText(Leanplum.getUserId());
        ((TextView)getView().findViewById(R.id.deviceId)).setText(Leanplum.getDeviceId());

        ((TextView)getView().findViewById(R.id.sdkVersion)).setText(BuildConfig.LEANPLUM_SDK_VERSION);
        ((TextView)getView().findViewById(R.id.apiHostName)).setText(env.getApiHostName());
        ((TextView)getView().findViewById(R.id.apiSSL)).setText(env.getApiSSL().toString());
        ((TextView)getView().findViewById(R.id.socketHostName)).setText(env.getSocketHostName());
        ((TextView)getView().findViewById(R.id.socketPort)).setText(String.valueOf(env.getSocketPort()));
    }

    private void initLeanplum() {
        InternalState state = InternalState.sharedState();

        LeanplumApp app = state.getApp();

        Leanplum.setAppIdForDevelopmentMode(
                app.getAppId(),
                BuildConfig.DEBUG ? app.getDevKey() : app.getProdKey()
        );

        LeanplumEnv env = state.getEnv();

        Leanplum.setSocketConnectionSettings(env.getSocketHostName(), env.getSocketPort());
        Leanplum.setApiConnectionSettings(env.getApiHostName(), "api", env.getApiSSL());
        Parser.parseVariablesForClasses(VariablesActivity.class);

        // Enable for GCM
//        LeanplumPushService.setGcmSenderId(LeanplumPushService.LEANPLUM_SENDER_ID);

        Leanplum.start(getContext());
    }

}
