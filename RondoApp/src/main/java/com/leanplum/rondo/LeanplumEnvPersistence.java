package com.leanplum.rondo;

import com.leanplum.rondo.models.LeanplumEnv;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LeanplumEnvPersistence {
    private static final String ENV_PRODUCTION = "api.leanplum.com";
    private static final String ENV_PRODUCTION_SOCKET = "dev.leanplum.com";

    private static final String ENV_STAGING = "leanplum-staging.appspot.com";
    private static final String ENV_STAGING_SOCKET = "dev-staging.leanplum.com";

    private static final String ENV_QA = "leanplum-qa-1372.appspot.com";
    private static final String ENV_QA_SOCKET = "dev-qa-1372.leanplum.com";

    private static final int SOCKET_PORT = 443;
    private static final boolean API_USE_SSL = true;

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
                equalTo("apiHostName", ENV_PRODUCTION).findFirst();
        realm.commitTransaction();
        return env;
    }

    static public LeanplumEnv qa() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumEnv env = realm.where(LeanplumEnv.class).
                equalTo("apiHostName", ENV_QA).findFirst();
        realm.commitTransaction();
        return env;
    }

    static public LeanplumEnv staging() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        LeanplumEnv env = realm.where(LeanplumEnv.class).
                equalTo("apiHostName", ENV_STAGING).findFirst();
        realm.commitTransaction();
        return env;
    }

    public static void seedDatabase() {
        Realm realm = Realm.getDefaultInstance();
        if (production() == null) {
            realm.beginTransaction();
            realm.copyToRealm(productionSeed());
            realm.commitTransaction();
        }
        if (qa() == null) {
            realm.beginTransaction();
            realm.copyToRealm(qaSeed());
            realm.commitTransaction();
        }
        if (staging() == null) {
            realm.beginTransaction();
            realm.copyToRealm(stagingSeed());
            realm.commitTransaction();
        }
    }

    static private LeanplumEnv productionSeed() {
        LeanplumEnv env = new LeanplumEnv();
        env.setApiHostName (ENV_PRODUCTION);
        env.setApiSSL(API_USE_SSL);
        env.setSocketHostName(ENV_PRODUCTION_SOCKET);
        env.setSocketPort(SOCKET_PORT);
        return env;
    }

    static private LeanplumEnv qaSeed() {
        LeanplumEnv env = new LeanplumEnv();
        env.setApiHostName(ENV_QA);
        env.setApiSSL(API_USE_SSL);
        env.setSocketHostName(ENV_QA_SOCKET);
        env.setSocketPort(SOCKET_PORT);
        return env;
    }

    static private LeanplumEnv stagingSeed() {
        LeanplumEnv env = new LeanplumEnv();
        env.setApiHostName(ENV_STAGING);
        env.setApiSSL(API_USE_SSL);
        env.setSocketHostName(ENV_STAGING_SOCKET);
        env.setSocketPort(SOCKET_PORT);
        return env;
    }
}
