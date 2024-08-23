package com.leanplum.rondo;

import androidx.multidex.MultiDexApplication;

import com.clevertap.android.pushtemplates.PushTemplateNotificationHandler;
import com.clevertap.android.sdk.CleverTapAPI;
import com.google.firebase.FirebaseApp;
import com.leanplum.Leanplum;
import com.leanplum.LeanplumActivityHelper;
import com.leanplum.annotations.Parser;
import com.leanplum.customtemplates.CustomInAppTemplatesKt;
import com.leanplum.customtemplates.RondoCustomTemplates;
import com.leanplum.internal.Log.Level;
import com.leanplum.rondo.models.InternalState;
import com.leanplum.rondo.models.LeanplumApp;
import com.leanplum.rondo.models.LeanplumEnv;
import com.leanplum.rondo.models.RondoProductionMode;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class RondoApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        RondoConfig.load(this);

        FirebaseApp.initializeApp(this);

        initCleverTap(); // have all setup done before Leanplum.setApplicationContext, which would initialise CT
        Leanplum.setLogLevel(Level.DEBUG);
        Leanplum.setApplicationContext(this);

        QueueActivityModel.INSTANCE.setListenerEnabled(true);

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

    private void initCleverTap() {
        RondoCustomTemplates.registerCleverTapTemplates();
        CleverTapAPI.setNotificationHandler(new PushTemplateNotificationHandler());
        // Register notification channels
        Leanplum.addCleverTapInstanceCallback(cleverTapInstance -> {

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

        RondoCustomTemplates.defineLeanplumTemplates();

        Map<String, Object> startAttributes = new HashMap<>();
        startAttributes.put("startAttributeInt", 1);
        startAttributes.put("startAttributeString", "stringValueFromStart");
        Leanplum.start(this, startAttributes);
    }
}
