package com.leanplum.rondo;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;
import com.leanplum.rondo.models.LeanplumEnv;

public class SdkQaFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sdk_qa, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        createTriggersButton();
        createAppInboxButton();
        createVariablesButton();
        createMessagesButton();
        createPushButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        populateVersionURLInfo();
    }

    private void populateVersionURLInfo() {
        InternalState state = InternalState.sharedState();
        LeanplumApp app = state.getApp();
        LeanplumEnv env = state.getEnv();

        TextView tv = getView().findViewById(R.id.sdkVersion);
        tv.setText(BuildConfig.LEANPLUM_SDK_VERSION);

        TextView tv1 = getView().findViewById(R.id.appName);
        tv1.setText(app.getDisplayName());

        TextView apiUrl = getView().findViewById(R.id.apiHostName);
        apiUrl.setText(env.getApiHostName());
    }

    private void createTriggersButton() {
        Button button = getView().findViewById(R.id.triggers);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), TriggersActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void createAppInboxButton() {
        Button button = getView().findViewById(R.id.appInbox);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), AppInboxActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void createVariablesButton() {
        Button button = getView().findViewById(R.id.variables);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), VariablesActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void createMessagesButton() {
        Button button = getView().findViewById(R.id.messages);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), MessagesActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void createPushButton() {
        Button button = getView().findViewById(R.id.push);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), PushActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
