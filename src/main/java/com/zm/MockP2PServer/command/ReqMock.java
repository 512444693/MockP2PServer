package com.zm.MockP2PServer.command;

import static com.zm.frame.log.Log.log;
import com.zm.message.Message;

/**
 * Created by zhangmin on 2017/8/25.
 */
public class ReqMock extends BaseMock {
    private Message message;
    private int cmdId;

    public ReqMock(String p2pStr, long lastModified) {
        super(p2pStr, lastModified);
        this.update(p2pStr);
    }

    @Override
    protected void update(String p2pStr) {
        log.debug("Req updated");
        this.message = new Message(p2pStr);
        message.encode();
        this.cmdId = message.getCmdID();
    }

    public int getCmdId() {
        return cmdId;
    }

    public Message getMessage() {
        return message;
    }
}
