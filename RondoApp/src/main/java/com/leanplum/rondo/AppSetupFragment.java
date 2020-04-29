package com.leanplum.rondo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.leanplum.Leanplum;
import com.leanplum.annotations.Parser;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;
import com.leanplum.rondo.models.LeanplumEnv;
import com.leanplum.rondo.models.RondoProductionMode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class AppSetupFragment extends Fragment {

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
        createChannelButton();
        setupProductionSwitch();
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

        ((TextView) getView().findViewById(R.id.appName)).setText(app.getDisplayName());
        ((TextView) getView().findViewById(R.id.appId)).setText(app.getAppId());
        ((TextView) getView().findViewById(R.id.devKey)).setText(app.getDevKey());
        ((TextView) getView().findViewById(R.id.prodKey)).setText(app.getProdKey());

        ((TextView) getView().findViewById(R.id.userId)).setText(Leanplum.getUserId());
        ((TextView) getView().findViewById(R.id.deviceId)).setText(Leanplum.getDeviceId());

        ((TextView) getView().findViewById(R.id.sdkVersion)).setText(BuildConfig.LEANPLUM_SDK_VERSION);
        ((TextView) getView().findViewById(R.id.apiHostName)).setText(env.getApiHostName());
        ((TextView) getView().findViewById(R.id.apiSSL)).setText(env.getApiSSL().toString());
        ((TextView) getView().findViewById(R.id.socketHostName)).setText(env.getSocketHostName());
        ((TextView) getView().findViewById(R.id.socketPort)).setText(String.valueOf(env.getSocketPort()));
    }

    private void setupProductionSwitch() {
        Switch productionModeSwitch = getView().findViewById(R.id.productionMode);
        final Context context = getContext();
        productionModeSwitch.setChecked(RondoProductionMode.isProductionMode(context));

        productionModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RondoProductionMode.setProductionMode(context, isChecked);
            }
        });
    }

    private void initLeanplum() {
        InternalState state = InternalState.sharedState();

        LeanplumApp app = state.getApp();

        if (RondoProductionMode.isProductionMode(getContext())) {
            Leanplum.setAppIdForProductionMode(
                    app.getAppId(),
                    app.getProdKey()
            );
        } else {
            Leanplum.setAppIdForDevelopmentMode(
                    app.getAppId(),
                    app.getDevKey()
            );
        }

        LeanplumEnv env = state.getEnv();

        Leanplum.setSocketConnectionSettings(env.getSocketHostName(), env.getSocketPort());
        Leanplum.setApiConnectionSettings(env.getApiHostName(), "api", env.getApiSSL());
        Parser.parseVariablesForClasses(VariablesFragment.class);

        // Enable for GCM
//        LeanplumPushService.setGcmSenderId(LeanplumPushService.LEANPLUM_SENDER_ID);

        Leanplum.start(getContext());
    }

    private void createChannelButton() {
        Button button = getView().findViewById(R.id.createChannel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = "https://"
                        + InternalState.sharedState().getEnv().getApiHostName()
                        + "/api?action=addAndroidNotificationChannel\n";
                final Map<String, String> params = new HashMap<>();
                InternalState state = InternalState.sharedState();
                params.put("appId", state.getApp().getAppId());
                params.put("clientKey", state.getApp().getDevKey());
                params.put("apiVersion", "1.0.6");
                params.put("id", "123");
                params.put("name", "rondo-channel");
                params.put("importance", "3");
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            performPostCall(url, params);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }

    public String performPostCall(String requestURL, Map<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                final Context context = getContext();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                final String displayText = "Success, channel created. " +
                        "200 received from server!";
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, displayText, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                final Context context = getContext();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                final String displayText = response;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context, displayText, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e) {
            final Context context = getContext();
            final String displayText = e.getMessage();
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(context, displayText, Toast.LENGTH_LONG).show();
                }
            });
        }

        return response;
    }

    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
