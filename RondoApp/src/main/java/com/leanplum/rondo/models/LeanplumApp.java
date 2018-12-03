package com.leanplum.rondo.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LeanplumApp extends RealmObject {
    @PrimaryKey
    private String displayName;

    private String appId;
    private String devKey;
    private String prodKey;

    public String getAppId() {
        return appId;
    }

    public String getDevKey() {
        return devKey;
    }

    public String getProdKey() {
        return prodKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setDevKey(String devKey) {
        this.devKey = devKey;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProdKey(String prodKey) {
        this.prodKey = prodKey;
    }
}
