package com.zm.MockP2PServer.task;

import com.zm.MockP2PServer.msg.body.TcpMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BasicThread;

import java.io.IOException;
import java.net.Socket;

import static com.zm.frame.log.Log.log;

public class LongTcpTask extends Task {
    private Socket socket;

    public LongTcpTask(int taskId, BasicThread thread, int time, Object arg) {
        super(taskId, thread, time);
        TcpMsgBody body = (TcpMsgBody) arg;
        socket = body.getSocket();
    }

    @Override
    public void processMsg(ThreadMsg threadMsg) {

    }

    @Override
    public void init() {
        log.debug("链接 " + socket.getInetAddress().getHostAddress() +
                ":" + socket.getPort());


    }

    @Override
    public void destroy() {
        log.debug(socket.getInetAddress().getHostAddress() +
                ":" + socket.getPort() + "链接断开");
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
