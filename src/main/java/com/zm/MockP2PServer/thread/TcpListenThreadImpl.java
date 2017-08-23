package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.ConnectionType;
import com.zm.MockP2PServer.common.MyDef;
import com.zm.MockP2PServer.msg.body.TcpMsgBody;
import com.zm.MockP2PServer.server.MockP2PServer;
import com.zm.frame.thread.thread.BasicThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.zm.frame.log.Log.log;

public class TcpListenThreadImpl extends BasicThread {

    private int port;
    private ConnectionType cntType =
            MockP2PServer.getInstance().getConfig().getCntType();

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
                int threadType = cntType == ConnectionType.TCP ?
                        MyDef.THREAD_TYPE_REC_AND_SEND : MyDef.THREAD_TYPE_LONG_TCP_REC_AND_SEND;

                sendThreadMsgTo(MyDef.MSG_TYPE_TCP_CNT,
                        new TcpMsgBody(socket), threadType);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }
}
