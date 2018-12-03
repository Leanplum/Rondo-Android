package com.leanplum.rondo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.leanplum.Leanplum;
import com.leanplum.annotations.Parser;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;
import com.leanplum.rondo.models.LeanplumEnv;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = new SdkQaFragment();
                                break;
                            case R.id.action_item2:
                                selectedFragment = new AdhocFragment();
                                break;
                            case R.id.action_item3:
                                selectedFragment = new AppSetupActivity();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new SdkQaFragment());
        transaction.commit();

        setUpInitialAppState();
        initLeanplum();
    }

    private void setUpInitialAppState() {
        InternalState state = InternalState.sharedState();
        RondoPreferences rondoPreferences = RondoPreferences.getRondoPreferences();
        state.setApp(rondoPreferences.getApp());
        state.setEnv(rondoPreferences.getEnv());
    }

    private void initLeanplum() {
        InternalState state = InternalState.sharedState();

        LeanplumApp app = state.getApp();

        Leanplum.setAppIdForDevelopmentMode(
                app.getAppId(),
                BuildConfig.DEBUG ? app.getDevKey() : app.getProdKey()
        );

        LeanplumEnv env = state.getEnv();

        Leanplum.setSocketConnectionSettings(env.getSocketHostName(), env.getSocketPort());
        Leanplum.setApiConnectionSettings(env.getApiHostName(), "api", env.getApiSSL());
        Parser.parseVariablesForClasses(VariablesActivity.class);

        // Enable for GCM
//        LeanplumPushService.setGcmSenderId(LeanplumPushService.LEANPLUM_SENDER_ID);

        Leanplum.start(this);
    }

}