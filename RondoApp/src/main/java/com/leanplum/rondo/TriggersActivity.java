package com.leanplum.rondo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.clevertap.android.sdk.leanplum.LeanplumCT;
import java.util.HashMap;
import java.util.Map;

public class TriggersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triggers);

        createEventTriggerButton();
        createStateTriggerButton();
        createUserAttributeChangeButton();
        createSessionLimitButton();
        createLifetimeLimitButton();
        createChainedIAMButton();
        createDifferentPrioritySameTimeAlertButton();
    }

    private void createEventTriggerButton() {
        Button button = findViewById(R.id.triggerEvent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeanplumCT.track("testEvent");
            }
        });
    }

    private void createStateTriggerButton() {
        Button button = findViewById(R.id.triggerState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeanplumCT.advanceTo("testState");
            }
        });
    }

    private void createUserAttributeChangeButton() {
        Button button = findViewById(R.id.userAttributeChange);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map attrib = new HashMap();
                attrib.put("age", Double.toString(Math.random()));
                LeanplumCT.setUserAttributes(attrib);
            }
        });
    }

    private void createSessionLimitButton() {
        Button button = findViewById(R.id.sessionLimit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeanplumCT.advanceTo("sessionLimit");
            }
        });
    }

    private void createLifetimeLimitButton() {
        Button button = findViewById(R.id.lifetimeLimit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeanplumCT.advanceTo("lifetimeLimit");
            }
        });
    }

    private void createChainedIAMButton() {
        Button button = findViewById(R.id.chainInApp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeanplumCT.track("chainedInApp");
            }
        });
    }

    private void createDifferentPrioritySameTimeAlertButton() {
        Button button = findViewById(R.id.diffPrioritySameTime);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeanplumCT.track("DifferentPrioritySameTime");
            }
        });
    }

}
