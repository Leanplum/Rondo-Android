package com.leanplum.rondo;

import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.leanplum.Var;

public class VariablesActivity extends AppCompatActivity {
    Var<String> composerName = Var.define("composerName", "Composer name");
    Var<String> compositionTitle = Var.define("compositionTitle", "Composition Title");
    Var<String> photoFile = Var.defineFile("photograph", null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variables);

        updateViewWithVariables();
    }

    private void updateViewWithVariables() {
        TextView name = findViewById(R.id.name);
        name.setText(composerName.stringValue);

        TextView title = findViewById(R.id.title);
        title.setText(compositionTitle.stringValue);

        if (photoFile.fileValue()!= null) {
            java.io.File imgFile = new java.io.File(photoFile.fileValue());
            if(imgFile.exists()) {
                ImageView myImage = findViewById(R.id.image);
                myImage.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }
}
