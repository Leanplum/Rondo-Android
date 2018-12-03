package com.leanplum.rondo;

import com.leanplum.rondo.models.LeanplumEnv;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LeanplumEnvPersistence {

    static void saveLeanplumEnv(LeanplumEnv env) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(env);
        realm.commitTransaction();
    }

    static ArrayList<LeanplumEnv> loadLeanplumEnvs() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<LeanplumEnv> envs = realm.where(LeanplumEnv.class)
                .findAll();
        envs.load();
        realm.commitTransaction();
        final ArrayList<LeanplumEnv> list = new ArrayList<LeanplumEnv>();
        for (LeanplumEnv env:envs) {
            list.add(env);
        }
        return list;
    }

    static public LeanplumEnv production() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumEnv env = realm.where(LeanplumEnv.class).
                equalTo("apiHostName", "api.leanplum.com").findFirst();
        realm.commitTransaction();
        return env;
    }

    static public LeanplumEnv qa() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumEnv env = realm.where(LeanplumEnv.class).
                equalTo("apiHostName", "leanplum-qa-1372.appspot.com").findFirst();
        realm.commitTransaction();
        return env;
    }

    public static void seedDatabase() {
        if (production() == null) {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealm(productionSeed());
            realm.copyToRealm(qaSeed());
            realm.commitTransaction();
        }
    }

    static private LeanplumEnv productionSeed() {
        LeanplumEnv env = new LeanplumEnv();
        env.setApiHostName ("api.leanplum.com");
        env.setApiSSL(true);
        env.setSocketHostName("dev.leanplum.com");
        env.setSocketPort(443);
        return env;
    }

    static private LeanplumEnv qaSeed() {
        LeanplumEnv env = new LeanplumEnv();
        env.setApiHostName("leanplum-qa-1372.appspot.com");
        env.setApiSSL(true);
        env.setSocketHostName("dev-qa.leanplum.com");
        env.setSocketPort(80);
        return env;
    }
}
