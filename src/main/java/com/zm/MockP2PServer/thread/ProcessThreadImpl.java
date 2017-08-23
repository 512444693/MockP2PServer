package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.MyDef;
import com.zm.MockP2PServer.msg.body.DataMsgBody;
import static com.zm.frame.log.Log.log;
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
            case MyDef.MSG_TYPE_REQ:
                byte[] data = ((DataMsgBody)msg.msgBody).getData();
                //log.debug("收到：\r\n" + new String(data));
                replayThreadMsg(msg, MyDef.MSG_TYPE_REPLY, new DataMsgBody(data));
                break;
            default:
                log.error("处理线程收到错误消息：" + msg.msgType);
        }

    }
}
