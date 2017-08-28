package com.zm.MockP2PServer.command;

/**
 * Created by zhangmin on 2017/8/25.
 */
public abstract class BaseMock {
    private String p2pStr;
    private long lastModified;

    public BaseMock(String p2pStr, long lastModified) {
        this.p2pStr = p2pStr;
        this.lastModified = lastModified;
    }

    public void update(String p2pStr, long lastModified) {
        if (lastModified > this.lastModified) {
            this.lastModified = lastModified;
            if (!p2pStr.trim().equals(this.p2pStr.trim())) {
                this.p2pStr = p2pStr;
                update(p2pStr);
            }
        }
    }

    protected abstract void update(String p2pStr);

    public String getP2pStr() {
        return p2pStr;
    }

    public long getLastModified() {
        return lastModified;
    }

}
