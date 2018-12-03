package com.leanplum.rondo;

import com.leanplum.rondo.models.LeanplumApp;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LeanplumAppPersistence {

    static void saveLeanplumApp(LeanplumApp app) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(app);
        realm.commitTransaction();
    }

    static ArrayList<LeanplumApp> loadLeanplumApps() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<LeanplumApp> apps = realm.where(LeanplumApp.class)
                .findAll();
        apps.load();
        realm.commitTransaction();
        final ArrayList<LeanplumApp> list = new ArrayList<LeanplumApp>();
        for (LeanplumApp app:apps) {
            list.add(app);
        }
        return list;
    }

    static public LeanplumApp rondoQAProduction() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
                equalTo("displayName", "Rondo QA Production").findFirst();
        realm.commitTransaction();
        return app;
    }

    public static void seedDatabase() {
        if (rondoQAProduction() == null) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(rondoQAProductionSeed());
            realm.commitTransaction();
        }
    }

    static private LeanplumApp rondoQAProductionSeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId("app_ve9UCNlqI8dy6Omzfu1rEh6hkWonNHVZJIWtLLt6aLs");
        app.setDevKey("dev_cKF5HMpLGqhbovlEGMKjgTuf8AHfr2Jar6rrnNhtzQ0");
        app.setProdKey("prod_D5ECYBLrRrrOYaFZvAFFHTg1JyZ2Llixe5s077Lw3rM");
        app.setDisplayName("Rondo QA Production");
        return app;
    }

}
