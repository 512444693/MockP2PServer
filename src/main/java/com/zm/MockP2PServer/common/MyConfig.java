package com.zm.MockP2PServer.common;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class MyConfig {
    private int port;
    private ConnectionType cntType;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ConnectionType getCntType() {
        return cntType;
    }

    public void setCntType(ConnectionType cntType) {
        this.cntType = cntType;
    }
}