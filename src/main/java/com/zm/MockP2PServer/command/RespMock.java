package com.zm.MockP2PServer.command;

import com.zm.message.Message;

import static com.zm.frame.log.Log.log;

/**
 * Created by zhangmin on 2017/8/25.
 */
public class RespMock extends BaseMock {
    private byte[] data;
    private String messageStr;
    private boolean containSuper;

    public RespMock(String p2pStr, long lastModified) {
        super(p2pStr, lastModified);
        this.update(p2pStr);
    }

    @Override
    protected void update(String p2pStr) {
        log.debug("Resp updated");
        Message msg = new Message(p2pStr);
        this.data = msg.encode();
        this.messageStr = msg.toString();
        this.containSuper = p2pStr.contains("super");
    }

    public byte[] getData() {
        //如果有super，需要重新编码
        if (containSuper) {
            Message msg = new Message(this.getP2pStr());
            this.data = msg.encode();
            this.messageStr = msg.toString();
        }
        return data;
    }

    public String getMessageStr() {
        return messageStr;
    }
}
