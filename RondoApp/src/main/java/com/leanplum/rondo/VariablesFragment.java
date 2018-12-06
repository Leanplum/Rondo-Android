package com.leanplum.rondo;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leanplum.Var;
import com.leanplum.annotations.File;

import androidx.fragment.app.Fragment;

public class VariablesFragment extends Fragment {
    Var<String> varString = Var.define("var_text", "Default value in code");
    Var<Number> varNumber = Var.define("var_number", null);
    Var<Boolean> varBoolean = Var.define("var_bool", false);
    Var<File> varFile = Var.define("var_file", null);



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_variables, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViewWithVariables();
    }

    private void updateViewWithVariables() {
        TextView string = getView().findViewById(R.id.varString);
        string.setText(varString.stringValue);

        TextView number = getView().findViewById(R.id.varNumber);
        number.setText(varNumber.stringValue);

        TextView bool = getView().findViewById(R.id.varBool);
        bool.setText(varBoolean.stringValue);

        TextView file = getView().findViewById(R.id.varFile);
        file.setText(varFile.stringValue);

        if (varFile.fileValue()!= null) {
            java.io.File imgFile = new java.io.File(varFile.fileValue());
            if(imgFile.exists()) {
                ImageView myImage = getView().findViewById(R.id.varFileImage);
                myImage.setImageURI(Uri.fromFile(imgFile));
            }
        }
    }
}
