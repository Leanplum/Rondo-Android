package com.leanplum.rondo;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;
import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.CleverTapAPI.LogLevel;
import com.clevertap.android.sdk.leanplum.LeanplumCT;
import com.clevertap.android.sdk.pushnotification.PushConstants;
import com.google.firebase.FirebaseApp;
import com.leanplum.rondo.models.InternalState;

import io.realm.Realm;

public class RondoApplication extends Application {

    /**
     * Update user info here
     */
    private String username = "some-qa-user@leanplum.com";

    @Override
    public void onCreate() {
        super.onCreate();

        RondoConfig.load(this);

        FirebaseApp.initializeApp(this);

        LeanplumCT.setLogLevel(LogLevel.VERBOSE);
        initCleverTap(); // have all setup done before Leanplum.setApplicationContext, which would initialise CT

        QueueActivityModel.INSTANCE.setListenerEnabled(true);

        ActivityLifecycleCallback.register(this);
        LeanplumCT.initWithContext(this);

        Realm.init(this);
        LeanplumAppPersistence.seedDatabase();
        LeanplumEnvPersistence.seedDatabase();

        setUpInitialAppState();
    }

    private void setUpInitialAppState() {
        InternalState state = InternalState.sharedState();
        RondoPreferences rondoPreferences = RondoPreferences.getRondoPreferences();
        state.setApp(rondoPreferences.getApp());
        state.setEnv(rondoPreferences.getEnv());
    }

    private void initCleverTap() {
        // Rondo App from Xiaomi console
        CleverTapAPI.changeXiaomiCredentials("2882303761518843048", "5601884323048");
        CleverTapAPI.enableXiaomiPushOn(PushConstants.XIAOMI_MIUI_DEVICES); // using ALL_DEVICES would spawn ":pushservice" process on non-Xiaomi devices
        CleverTapAPI.setNotificationHandler(new PushTemplateNotificationHandler());
        // Register notification channels
//        Leanplum.addCleverTapInstanceCallback(cleverTapInstance -> {
            CleverTapAPI.createNotificationChannel(
                RondoApplication.this,
                "YourChannelId",
                "Your Channel Name",
                "Your Channel Description",
                5,
                true);
//        });
    }

//    private void initLeanplum() {
//        InternalState state = InternalState.sharedState();
//
//        LeanplumApp app = state.getApp();
//
//        if (RondoProductionMode.isProductionMode(this)) {
//            Leanplum.setAppIdForProductionMode(
//                    app.getAppId(),
//                    app.getProdKey()
//            );
//        } else {
//            Leanplum.setAppIdForDevelopmentMode(
//                    app.getAppId(),
//                    app.getDevKey()
//            );
//        }
//
//        LeanplumEnv env = state.getEnv();
//
//        Leanplum.setSocketConnectionSettings(env.getSocketHostName(), env.getSocketPort());
//        Leanplum.setApiConnectionSettings(env.getApiHostName(), "api", env.getApiSSL());
//        Parser.parseVariablesForClasses(VariablesFragment.class);
//
//        Map<String, Object> startAttributes = new HashMap<>();
//        startAttributes.put("startAttributeInt", 1);
//        startAttributes.put("startAttributeString", "stringValueFromStart");
//        Leanplum.start(this, startAttributes);
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            MultiDex.install(this); // enable multiDex for older devices
    }
}
