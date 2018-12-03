package com.leanplum.rondo.models;

public class InternalState {
    private LeanplumApp app;
    private LeanplumEnv env;

    private static final InternalState instance = new InternalState();

    static public InternalState sharedState() {
        return instance;
    }

    public void setApp(LeanplumApp app) {
        this.app = app;
    }

    public void setEnv(LeanplumEnv env) {
        this.env = env;
    }

    public LeanplumApp getApp() {
        return app;
    }

    public LeanplumEnv getEnv() {
        return env;
    }
}
