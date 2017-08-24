package com.zm.MockP2PServer.task;

import com.zm.MockP2PServer.common.MyDef;
import com.zm.MockP2PServer.msg.body.DataMsgBody;
import com.zm.MockP2PServer.msg.body.TcpMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BasicThread;
import com.zm.utils.BU;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import static com.zm.MockP2PServer.common.MyDef.MAX_PACKET_SIZE;
import static com.zm.frame.log.Log.log;

public class TcpTask extends Task {

    private Socket socket;
    BufferedInputStream in = null;
    private BufferedOutputStream out = null;

    public TcpTask(int taskId, BasicThread thread, int time, Object arg) {
        super(taskId, thread, time);
        TcpMsgBody body = (TcpMsgBody) arg;
        socket = body.getSocket();
    }

    @Override
    public void processMsg(ThreadMsg threadMsg) {
        if(threadMsg.msgType == MyDef.MSG_TYPE_REPLY) {
            byte[] data = ((DataMsgBody)threadMsg.msgBody).getData();
            //Log.log.debug("收到处理线程消息：" + new String(data));
            send(data);
        } else {
            log.error("TCPTask 收到错误消息类型：" + threadMsg.msgType);
        }
    }

    @Override
    public void init() {
        log.debug("链接 " + socket.getInetAddress().getHostAddress() +
                ":" + socket.getPort());
        byte[] data = rec();
        if (data != null) {
            sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new DataMsgBody(data), MyDef.THREAD_TYPE_PROCESS);
        }
    }

    private byte[] rec(){
        byte[] buffer = new byte[MAX_PACKET_SIZE];
        byte[] data = null;
        try {
            in = new BufferedInputStream(socket.getInputStream());
            int len = in.read(buffer);
            if (len > 0) {
                data = BU.subByte(buffer, 0, len);
            } else { // len < -1，链接被关闭，len == 0 ?
                throw new IOException("链接被关闭");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            //接收数据异常关闭链接
            removeSelfFromThread();
        }
        return data;
    }

    private void send(byte[] data){
        try {
            out = new BufferedOutputStream(socket.getOutputStream());
            out.write(data);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            //发送完成或异常都关闭链接
            removeSelfFromThread();
        }
    }

    @Override
    public void destroy() {
        log.debug(socket.getInetAddress().getHostAddress() +
                ":" + socket.getPort() + " 链接断开");
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            log.error("关闭链接异常：" + e.getMessage());
        }
    }
}
