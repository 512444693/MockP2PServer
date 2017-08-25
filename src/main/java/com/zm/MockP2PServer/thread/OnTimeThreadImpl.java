package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.D;
import com.zm.frame.thread.thread.NoBlockingThread;

public class OnTimeThreadImpl extends NoBlockingThread {

    private long lastCheckTaskTime = System.currentTimeMillis();
    private long lastReadMockFileTime = System.currentTimeMillis();

    public OnTimeThreadImpl(int threadType, int threadId, Object arg) {
        super(threadType, threadId, (int) arg);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void afterProcessMsg() {
        long timeNow = System.currentTimeMillis();
        //每秒发送消息，处理一次任务超时
        if ((timeNow - lastCheckTaskTime) >= (1 * 1000)) {
            sendThreadMsgTo(D.MSG_TYPE_CHECK_TASK_TIMEOUT, null, D.THREAD_TYPE_REC_AND_SEND);
            lastCheckTaskTime = timeNow;
        }

        if ((timeNow - lastReadMockFileTime) >= (10 * 1000)) {
            sendThreadMsgTo(D.MSG_TYPE_CHECK_FILE, null, D.THREAD_TYPE_PROCESS);
            lastReadMockFileTime = timeNow;
        }
    }
}
