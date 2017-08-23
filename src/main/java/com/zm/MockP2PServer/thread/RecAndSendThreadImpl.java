package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.MyDef;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.msg.ThreadMsgBody;
import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BlockingThread;


/**
 * Created by zhangmin on 2017/8/23.
 */
public class RecAndSendThreadImpl extends BlockingThread {
    public RecAndSendThreadImpl(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void threadProcessMsg(ThreadMsg msg) {
        //Log.log.debug("收发线程的task数量：" + this.tasks.entrySet().size());
        ThreadMsgBody body = msg.msgBody;
        switch (msg.msgType) {
            case MyDef.MSG_TYPE_UDP_CNT:
                Task task = addTask(MyDef.TASK_TYPE_UDP, 10, body);
                task.init();
                break;
            default:
                super.processMsg(msg);
        }
    }
}
