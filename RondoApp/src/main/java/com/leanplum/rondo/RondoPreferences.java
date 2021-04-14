package com.leanplum.rondo;

import com.leanplum.rondo.models.LeanplumApp;
import com.leanplum.rondo.models.LeanplumEnv;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RondoPreferences extends RealmObject {

    @PrimaryKey
    private int id;
    private LeanplumApp app;
    private LeanplumEnv env;

    static void updateRondoPreferencesWithApp(LeanplumApp app) {
        RondoPreferences prefs = RondoPreferences.getRondoPreferences();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        prefs.app = app;
        realm.commitTransaction();
    }

    static void updateRondoPreferencesWithEnv(LeanplumEnv env) {
        RondoPreferences prefs = RondoPreferences.getRondoPreferences();

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        prefs.env = env;
        realm.commitTransaction();
    }

    static RondoPreferences getRondoPreferences() {
        Realm realm = Realm.getDefaultInstance();
        RondoPreferences rondoPreferences = realm.where(RondoPreferences.class).findFirst();
        if (rondoPreferences == null) {
            LeanplumApp app = LeanplumAppPersistence.rondoAutomation();
            LeanplumEnv env = LeanplumEnvPersistence.production();

            realm.beginTransaction();
            rondoPreferences = new RondoPreferences();
            rondoPreferences.id = 1;

            rondoPreferences.app = app;
            rondoPreferences.env = env;
            realm.copyToRealm(rondoPreferences);
            realm.commitTransaction();
        }
        return rondoPreferences;
    }

    public LeanplumEnv getEnv() {
        return env;
    }

    public LeanplumApp getApp() {
        return app;
    }
}
