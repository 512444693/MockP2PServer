package com.zm.MockP2PServer.thread;


import com.zm.frame.thread.thread.NoBlockingThread;

public class LongTcpRecAndSendThreadImpl extends NoBlockingThread {
    public LongTcpRecAndSendThreadImpl(int threadType, int threadId, Object arg) {
        super(threadType, threadId, (int) arg);
    }

    @Override
    protected void init() {

    }


    @Override
    protected void afterProcessMsg() {

    }
}
