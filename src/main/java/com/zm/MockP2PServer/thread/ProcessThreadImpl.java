package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.D;
import com.zm.MockP2PServer.msg.body.DataMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.thread.BlockingThread;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class ProcessThreadImpl extends BlockingThread {
    public ProcessThreadImpl(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void threadProcessMsg(ThreadMsg msg) {
        switch(msg.msgType) {
            case D.MSG_TYPE_REQ:
                byte[] data = ((DataMsgBody)msg.msgBody).getData();
                //log.debug("收到：\r\n" + new String(data));
                replayThreadMsg(msg, D.MSG_TYPE_REPLY, new DataMsgBody(data));
                break;
            default:
                super.threadProcessMsg(msg);
        }

    }
}
