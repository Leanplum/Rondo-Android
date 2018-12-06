package com.leanplum.rondo;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.leanplum.Leanplum;
import com.leanplum.LeanplumInbox;
import com.leanplum.LeanplumInboxMessage;
import com.leanplum.rondo.adapters.LeanplumAppAdapter;
import com.leanplum.rondo.adapters.LeanplumInboxMessageAdapter;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;

import java.io.File;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class AppInboxFragment extends
        Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_app_inbox, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        reloadData();
    }

    private void reloadData() {
        LeanplumInbox inbox = Leanplum.getInbox();
//        for (LeanplumInboxMessage message : inbox.allMessages()) {
//            createTableRow(message.getTitle(), message.getSubtitle(), message.getImageFilePath());
//            message.read(); //todo; abstract
//        }

        final ListView listview = getView().findViewById(R.id.listview);
        final ArrayList<LeanplumInboxMessage> list = new ArrayList<>(inbox.allMessages());
        final LeanplumInboxMessageAdapter adapter = new LeanplumInboxMessageAdapter(getContext(), list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
//                final LeanplumApp app = (LeanplumApp) parent.getItemAtPosition(position);
//                InternalState.sharedState().setApp(app);
//                RondoPreferences.updateRondoPreferencesWithApp(app);
//                finish();
            }

        });
    }


    public void createTableRow(String title, String subtitle, String imageFilePath) {
        LinearLayout tl = getView().findViewById(R.id.table);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView titleView = new TextView(getContext());
        titleView.setLayoutParams(lp);
        titleView.setText(title);

        TextView subtitleView= new TextView(getContext());
        subtitleView.setLayoutParams(lp);
        subtitleView.setText(subtitle);

        if (imageFilePath != null) {
            File imgFile = new  File(imageFilePath);
            if(imgFile.exists()) {
                ImageView myImage = new ImageView(getContext());
                myImage.setImageURI(Uri.fromFile(imgFile));
                tl.addView(myImage);
            }
        }
        tl.addView(titleView);
        tl.addView(subtitleView);

        Button bt = new Button(getContext());
        bt.setText("A Button");
        bt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tl.addView(bt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
