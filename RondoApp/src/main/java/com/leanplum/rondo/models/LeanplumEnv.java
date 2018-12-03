package com.leanplum.rondo.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LeanplumEnv extends RealmObject {
    @PrimaryKey
    private String apiHostName;
    private Boolean apiSSL;
    private String socketHostName;
    private int socketPort;

    public String getApiHostName() {
        return apiHostName;
    }

    public Boolean getApiSSL() {
        return apiSSL;
    }

    public String getSocketHostName() {
        return socketHostName;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public void setApiHostName(String apiHostName) {
        this.apiHostName = apiHostName;
    }

    public void setApiSSL(Boolean apiSSL) {
        this.apiSSL = apiSSL;
    }

    public void setSocketHostName(String socketHostName) {
        this.socketHostName = socketHostName;
    }

    public void setSocketPort(int socketPort) {
        this.socketPort = socketPort;
    }
}


