package com.leanplum.rondo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.leanplum.Leanplum;
import com.leanplum.rondo.models.InternalState;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PushActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);
        createChannelButton();
        final ListView listview = findViewById(R.id.listview);

        String[] values = new String[] { "pushRender", "pushAction", "pushImage",
                "pushExistingAction", "pushURL", "pushOptions", "pushLocal", "pushLocalCancel",
                "pushMuted", "pushLocalSamePriorityTime", "pushLocalSamePriorityDifferentTime"};

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
                Leanplum.track(item);
            }

        });
    }

    private void createChannelButton() {
        Button button = findViewById(R.id.createChannel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = "https://www.leanplum.com/api?action=addAndroidNotificationChannel\n";
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
                        try  {
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
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Toast.makeText(this, "Success!",
                        Toast.LENGTH_LONG).show();
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                Toast.makeText(this, "Error!",
                        Toast.LENGTH_LONG).show();
                response="";
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return response;
    }
    private String getPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
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
