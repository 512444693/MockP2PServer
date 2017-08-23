package com.zm.MockP2PServer.msg.body;

import com.zm.frame.thread.msg.ThreadMsgBody;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class DataMsgBody extends ThreadMsgBody {
    private byte[] data;

    public DataMsgBody(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
