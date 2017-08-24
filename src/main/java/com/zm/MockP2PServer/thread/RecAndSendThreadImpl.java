package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.ConnectionType;
import com.zm.MockP2PServer.common.D;
import com.zm.MockP2PServer.server.MockP2PServer;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.thread.BlockingThread;

import static com.zm.MockP2PServer.common.D.*;


/**
 * Created by zhangmin on 2017/8/23.
 */
public class RecAndSendThreadImpl extends BlockingThread {

    private ConnectionType cntType =
            MockP2PServer.getInstance().getConfig().getCntType();

    public RecAndSendThreadImpl(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void threadProcessMsg(ThreadMsg msg) {
        //log.debug("收发线程的task数量：" + this.tasks.entrySet().size());
        ThreadMsgBody body = msg.msgBody;
        switch (msg.msgType) {
            case MSG_TYPE_UDP_CNT:
                addTask(TASK_TYPE_UDP, 10, body);
                break;
            case MSG_TYPE_TCP_CNT:
                if (cntType == ConnectionType.TCP) {
                    addTask(TASK_TYPE_TCP, 10, body);
                } else if (cntType == ConnectionType.LONG_TCP) {
                    addTask(TASK_TYPE_LONG_TCP, D.NONE,body);
                }
                break;
            default:
                super.threadProcessMsg(msg);
        }
    }
}
