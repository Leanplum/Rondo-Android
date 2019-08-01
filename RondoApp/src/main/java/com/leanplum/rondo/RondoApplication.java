package com.leanplum.rondo;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.annotations.Parser;
import com.leanplum.callbacks.StartCallback;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;

import com.leanplum.rondo.models.LeanplumEnv;
import com.leanplum.rondo.models.RondoProductionMode;
import io.realm.Realm;

public class RondoApplication extends Application {

    /**
     * Update user info here
     */
    private String username = "some-qa-user@leanplum.com";

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);

        Leanplum.setApplicationContext(this);

        Parser.parseVariables(this);
        LeanplumActivityHelper.enableLifecycleCallbacks(this);

        Realm.init(this);
        LeanplumAppPersistence.seedDatabase();
        LeanplumEnvPersistence.seedDatabase();

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

        if (RondoProductionMode.isProductionMode(this)) {
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

        Leanplum.start(this);
    }

}
