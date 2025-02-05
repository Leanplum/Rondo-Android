package com.leanplum.rondo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (item -> {
                    Fragment selectedFragment;
                    switch (item.getItemId()) {
                        case R.id.sdq_qa:
                            selectedFragment = new SdkQaFragment();
                            break;
                        case R.id.adhoc:
                            selectedFragment = new AdhocFragment();
                            break;
                        case R.id.app_inbox:
                            selectedFragment = new AppInboxFragment();
                            break;
                        case R.id.variables:
                            selectedFragment = new VariablesFragment();
                            break;
                        case R.id.app_setup:
                        default:
                            selectedFragment = new AppSetupFragment();
                            break;
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    transaction.commit();
                    return true;
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new AppSetupFragment());
        transaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        CleverTapAPI cleverTapAPI = RondoApplication.cleverTapApi;
        if (cleverTapAPI != null && intent != null && intent.getExtras() != null) {
            cleverTapAPI.pushNotificationClickedEvent(intent.getExtras());
        }
    }
}
