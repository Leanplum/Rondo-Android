package com.leanplum.rondo;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leanplum.Leanplum;
import com.leanplum.LeanplumInbox;
import com.leanplum.LeanplumInboxMessage;

import java.io.File;

public class AppInboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_inbox);

        LeanplumInbox inbox = Leanplum.getInbox();
        for (LeanplumInboxMessage message : inbox.allMessages()) {
            createTableRow(message.getTitle(), message.getSubtitle(), message.getImageFilePath());
            message.read(); //todo; abstract
        }

    }

    public void createTableRow(String title, String subtitle, String imageFilePath) {
        LinearLayout tl = findViewById(R.id.table);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView titleView = new TextView(this);
        titleView.setLayoutParams(lp);
        titleView.setText(title);

        TextView subtitleView= new TextView(this);
        subtitleView.setLayoutParams(lp);
        subtitleView.setText(subtitle);

        if (imageFilePath != null) {
            File imgFile = new  File(imageFilePath);
            if(imgFile.exists()) {
                ImageView myImage = new ImageView(this);
                myImage.setImageURI(Uri.fromFile(imgFile));
                tl.addView(myImage);
            }
        }
        tl.addView(titleView);
        tl.addView(subtitleView);

        Button bt = new Button(this);
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
