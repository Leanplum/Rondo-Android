package com.leanplum.rondo;

import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.variables.Var;
import java.lang.ref.WeakReference;


public class VariablesFragment extends Fragment {
    Var<String> varString;
    Var<Number> varNumber;
    Var<Boolean> varBoolean;

    private Thread signatureVerificationThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        varString = CleverTapAPI.getDefaultInstance(getActivity()).defineVariable("var_text", "Default value in code");
        varNumber = CleverTapAPI.getDefaultInstance(getActivity()).defineVariable("var_number", null);
        varBoolean = CleverTapAPI.getDefaultInstance(getActivity()).defineVariable("var_bool", false);

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
    }

    public void onStop() {
        super.onStop();
        if (signatureVerificationThread != null) {
            signatureVerificationThread.interrupt();
            signatureVerificationThread = null;
        }
    }
}
