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

    private static final String APP_RONDO_AUTOMATION = "Rondo Automation";
    private static final String APP_RONDO_AUTOMATION_ID = "app_UQcFGVeXzOCVsovrlUebad9R67hFJqzDegfQPZRnVZM";
    private static final String APP_RONDO_AUTOMATION_DEVKEY = "dev_b9qX0tcazL5PCQFuZ7pxsfT6XHA7xQkaFtYVrgt4Kq0";
    private static final String APP_RONDO_AUTOMATION_PRODKEY = "prod_lL8RSFzmHy0iVYXQpzjUVEHDlaUz5idT0H7BVs6Bn1Q";

    private static final String APP_MUSALA_QA = "Musala QA";
    private static final String APP_MUSALA_QA_ID = "app_qA781mPlJYjzlZLDlTh68cdNDUOf31kcTg1TCbSXSS0";
    private static final String APP_MUSALA_QA_DEVKEY = "dev_WqNqX0qOOHyTEQtwKXs5ldhqErHfixvcSAMlYgyIL0U";
    private static final String APP_MUSALA_QA_PRODKEY = "prod_kInQHXLJ0Dju7QJRocsD5DYMdYAVbdGGwhl6doTfH0k";

    private static final String APP_QA_RONDO_AUTOMATION_ENV_QA = "QA Rondo Automation";
    private static final String APP_QA_RONDO_AUTOMATION_ENV_QA_ID = "app_lgRD9ao8xGOJBYSjSirleLXDFPj0x3iShUOpHGzUQgE";
    private static final String APP_QA_RONDO_AUTOMATION_ENV_QA_DEVKEY = "dev_QuUiracnzZDhOaGRjYZmOCOjUxPL1EM8W6HQwtWaM8E";
    private static final String APP_QA_RONDO_AUTOMATION_ENV_QA_PRODKEY = "prod_A3s5VjGs4J3gS9LkdgKVWnXdm9HlfegxbKQffoEFq08";

    private static final String APP_QA_MI_RONDO_AUTOMATION_ENV_QA = "QA Mi Rondo Automation";
    private static final String APP_QA_MI_RONDO_AUTOMATION_ENV_QA_ID = "app_melem6LLekJRgDyY7cHJgauxaaRTt9HAJu1LyLiHrTk";
    private static final String APP_QA_MI_RONDO_AUTOMATION_ENV_QA_DEVKEY = "dev_7I9EdRn6PuCwN8fhAND6HTr9xCZQteAZU20dkeVgOaw";
    private static final String APP_QA_MI_RONDO_AUTOMATION_ENV_QA_PRODKEY = "prod_nx1oKANVsPPDLj0XAXckWlaedA0CFxeGuFatl48vUSc";

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
            realm.copyToRealmOrUpdate(rondoAutomationSeed());
            realm.copyToRealmOrUpdate(musalaQASeed());
            realm.copyToRealmOrUpdate(qaRondoAutomationSeed());
            realm.copyToRealmOrUpdate(qaMiRondoAutomationSeed());
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

    static public LeanplumApp rondoAutomation() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
                equalTo("displayName", APP_RONDO_AUTOMATION).findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp rondoAutomationSeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId(APP_RONDO_AUTOMATION_ID);
        app.setDevKey(APP_RONDO_AUTOMATION_DEVKEY);
        app.setProdKey(APP_RONDO_AUTOMATION_PRODKEY);
        app.setDisplayName(APP_RONDO_AUTOMATION);
        return app;
    }

    static public LeanplumApp qaRondoAutomation() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
            equalTo("displayName", APP_QA_RONDO_AUTOMATION_ENV_QA).findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp qaRondoAutomationSeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId(APP_QA_RONDO_AUTOMATION_ENV_QA_ID);
        app.setDevKey(APP_QA_RONDO_AUTOMATION_ENV_QA_DEVKEY);
        app.setProdKey(APP_QA_RONDO_AUTOMATION_ENV_QA_PRODKEY);
        app.setDisplayName(APP_QA_RONDO_AUTOMATION_ENV_QA);
        return app;
    }

    static public LeanplumApp qaMiRondoAutomation() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumApp app = realm.where(LeanplumApp.class).
            equalTo("displayName", APP_QA_MI_RONDO_AUTOMATION_ENV_QA).findFirst();
        realm.commitTransaction();
        return app;
    }

    static private LeanplumApp qaMiRondoAutomationSeed() {
        LeanplumApp app = new LeanplumApp();
        app.setAppId(APP_QA_MI_RONDO_AUTOMATION_ENV_QA_ID);
        app.setDevKey(APP_QA_MI_RONDO_AUTOMATION_ENV_QA_DEVKEY);
        app.setProdKey(APP_QA_MI_RONDO_AUTOMATION_ENV_QA_PRODKEY);
        app.setDisplayName(APP_QA_MI_RONDO_AUTOMATION_ENV_QA);
        return app;
    }

}
