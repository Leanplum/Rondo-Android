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

    public static void seedDatabase() {
        if (rondoQAProduction() == null) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(rondoQAProductionSeed());
            realm.copyToRealmOrUpdate(rondoQAAutomationSeed());
            realm.copyToRealmOrUpdate(musalaQASeed());
            realm.commitTransaction();
        }
    }

    static public LeanplumApp rondoQAProduction() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
                equalTo("displayName", "Rondo QA Production").findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp rondoQAProductionSeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId("app_ve9UCNlqI8dy6Omzfu1rEh6hkWonNHVZJIWtLLt6aLs");
        app.setDevKey("dev_cKF5HMpLGqhbovlEGMKjgTuf8AHfr2Jar6rrnNhtzQ0");
        app.setProdKey("prod_D5ECYBLrRrrOYaFZvAFFHTg1JyZ2Llixe5s077Lw3rM");
        app.setDisplayName("Rondo QA Production");
        return app;
    }

    static public LeanplumApp musalaQA() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
                equalTo("displayName", "Musala QA").findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp musalaQASeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId("app_qA781mPlJYjzlZLDlTh68cdNDUOf31kcTg1TCbSXSS0");
        app.setDevKey("dev_WqNqX0qOOHyTEQtwKXs5ldhqErHfixvcSAMlYgyIL0U");
        app.setProdKey("prod_kInQHXLJ0Dju7QJRocsD5DYMdYAVbdGGwhl6doTfH0k");
        app.setDisplayName("Musala QA");
        return app;
    }

    static public LeanplumApp rondoQAAutomation() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
                equalTo("displayName", "Rondo QA Automation").findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp rondoQAAutomationSeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId("app_UQcFGVeXzOCVsovrlUebad9R67hFJqzDegfQPZRnVZM");
        app.setDevKey("dev_b9qX0tcazL5PCQFuZ7pxsfT6XHA7xQkaFtYVrgt4Kq0");
        app.setProdKey("prod_lL8RSFzmHy0iVYXQpzjUVEHDlaUz5idT0H7BVs6Bn1Q");
        app.setDisplayName("Rondo QA Automation");
        return app;
    }

}
