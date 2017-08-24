package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.D;
import com.zm.MockP2PServer.msg.body.TcpMsgBody;
import com.zm.frame.thread.thread.BasicThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.zm.frame.log.Log.log;

public class TcpListenThreadImpl extends BasicThread {

    private int port;

    private ServerSocket serverSocket;
    private Socket socket;

    public TcpListenThreadImpl(int threadType, int threadId, Object arg) {
        super(threadType, threadId);
        this.port = (int) arg;
    }

    @Override
    protected void init() {}

    @Override
    protected void process() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                socket = serverSocket.accept();
                sendThreadMsgTo(D.MSG_TYPE_TCP_CNT,
                        new TcpMsgBody(socket), D.THREAD_TYPE_REC_AND_SEND);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }
}
