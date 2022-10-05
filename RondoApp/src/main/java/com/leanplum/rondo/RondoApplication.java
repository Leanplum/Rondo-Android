package com.leanplum.rondo;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import androidx.multidex.MultiDex;
import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.pushnotification.PushConstants;
import com.google.firebase.FirebaseApp;
import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.annotations.Parser;
import com.leanplum.internal.Log.Level;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;

import com.leanplum.rondo.models.LeanplumEnv;
import com.leanplum.rondo.models.RondoProductionMode;
import io.realm.Realm;
import java.util.HashMap;
import java.util.Map;

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

        Leanplum.setApplicationContext(this);
        Leanplum.setLogLevel(Level.DEBUG);

        QueueActivityModel.INSTANCE.setListenerEnabled(true);

        Parser.parseVariables(this);
        LeanplumActivityHelper.enableLifecycleCallbacks(this);

        Realm.init(this);
        LeanplumAppPersistence.seedDatabase();
        LeanplumEnvPersistence.seedDatabase();

        setUpInitialAppState();
        initCleverTap();
        initLeanplum();
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
        Leanplum.onCleverTapInstanceInitialized(cleverTapInstance -> {
            CleverTapAPI.createNotificationChannel(
                RondoApplication.this,
                "YourChannelId",
                "Your Channel Name",
                "Your Channel Description",
                5,
                true);
        });
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

        Map<String, Object> startAttributes = new HashMap<>();
        startAttributes.put("startAttributeInt", 1);
        startAttributes.put("startAttributeString", "stringValueFromStart");
        Leanplum.start(this, startAttributes);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            MultiDex.install(this); // enable multiDex for older devices
    }
}
