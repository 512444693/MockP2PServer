package com.zm.MockP2PServer.msg.body;

import com.zm.frame.thread.msg.ThreadMsgBody;
import java.net.Socket;

public class TcpMsgBody extends ThreadMsgBody {
    private Socket socket;

    public TcpMsgBody(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
