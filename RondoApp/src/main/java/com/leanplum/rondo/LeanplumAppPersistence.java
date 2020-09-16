package com.leanplum.rondo;

import com.leanplum.rondo.models.LeanplumApp;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LeanplumAppPersistence {
    private static final String APP_RONDO_QA_PRODUCTION = "Rondo QA Production";
    private static final String APP_RONDO_QA_PRODUCTION_ID = "app_ve9UCNlqI8dy6Omzfu1rEh6hkWonNHVZJIWtLLt6aLs";
    private static final String APP_RONDO_QA_PRODUCTION_DEVKEY = "dev_cKF5HMpLGqhbovlEGMKjgTuf8AHfr2Jar6rrnNhtzQ0";
    private static final String APP_RONDO_QA_PRODUCTION_PRODKEY = "prod_D5ECYBLrRrrOYaFZvAFFHTg1JyZ2Llixe5s077Lw3rM";

    private static final String APP_RONDO_QA_AUTOMATION = "Rondo QA Automation";
    private static final String APP_RONDO_QA_AUTOMATION_ID = "app_UQcFGVeXzOCVsovrlUebad9R67hFJqzDegfQPZRnVZM";
    private static final String APP_RONDO_QA_AUTOMATION_DEVKEY = "dev_b9qX0tcazL5PCQFuZ7pxsfT6XHA7xQkaFtYVrgt4Kq0";
    private static final String APP_RONDO_QA_AUTOMATION_PRODKEY = "prod_lL8RSFzmHy0iVYXQpzjUVEHDlaUz5idT0H7BVs6Bn1Q";

    private static final String APP_MUSALA_QA = "Musala QA";
    private static final String APP_MUSALA_QA_ID = "app_qA781mPlJYjzlZLDlTh68cdNDUOf31kcTg1TCbSXSS0";
    private static final String APP_MUSALA_QA_DEVKEY = "dev_WqNqX0qOOHyTEQtwKXs5ldhqErHfixvcSAMlYgyIL0U";
    private static final String APP_MUSALA_QA_PRODKEY = "prod_kInQHXLJ0Dju7QJRocsD5DYMdYAVbdGGwhl6doTfH0k";

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
        final ArrayList<LeanplumApp> list = new ArrayList<>();
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
                equalTo("displayName", APP_RONDO_QA_PRODUCTION).findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp rondoQAProductionSeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId(APP_RONDO_QA_PRODUCTION_ID);
        app.setDevKey(APP_RONDO_QA_PRODUCTION_DEVKEY);
        app.setProdKey(APP_RONDO_QA_PRODUCTION_PRODKEY);
        app.setDisplayName(APP_RONDO_QA_PRODUCTION);
        return app;
    }

    static public LeanplumApp musalaQA() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
                equalTo("displayName", APP_MUSALA_QA).findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp musalaQASeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId(APP_MUSALA_QA_ID);
        app.setDevKey(APP_MUSALA_QA_DEVKEY);
        app.setProdKey(APP_MUSALA_QA_PRODKEY);
        app.setDisplayName(APP_MUSALA_QA);
        return app;
    }

    static public LeanplumApp rondoQAAutomation() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
                equalTo("displayName", APP_RONDO_QA_AUTOMATION).findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp rondoQAAutomationSeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId(APP_RONDO_QA_AUTOMATION_ID);
        app.setDevKey(APP_RONDO_QA_AUTOMATION_DEVKEY);
        app.setProdKey(APP_RONDO_QA_AUTOMATION_PRODKEY);
        app.setDisplayName(APP_RONDO_QA_AUTOMATION);
        return app;
    }

}
