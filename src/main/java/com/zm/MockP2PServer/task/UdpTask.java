package com.zm.MockP2PServer.task;

import com.zm.MockP2PServer.common.MyDef;
import com.zm.MockP2PServer.msg.body.DataMsgBody;
import com.zm.MockP2PServer.msg.body.UdpMsgBody;
import com.zm.frame.log.Log;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.task.Task;
import com.zm.frame.thread.thread.BasicThread;
import com.zm.utils.BU;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class UdpTask extends Task {
    private DatagramPacket packet;
    private DatagramSocket socket;
    public UdpTask(int taskId, BasicThread thread, int time, Object arg) {
        super(taskId, thread, time);
        UdpMsgBody body = (UdpMsgBody) arg;
        packet = body.getPacket();
        socket = body.getSocket();
    }

    @Override
    public void processMsg(ThreadMsg threadMsg) {
        if(threadMsg.msgType == MyDef.MSG_TYPE_REPLY) {
            byte[] data = ((DataMsgBody)threadMsg.msgBody).getData();
            //Log.log.debug("收到处理线程消息：" + new String(data));
            try {
                socket.send(new DatagramPacket(
                        data, data.length, packet.getAddress(), packet.getPort()
                ));
            } catch (IOException e) {
                Log.log.error(e.getMessage());
            } finally {
                removeSelfFromThread();
            }
        } else {
            Log.log.error("UdpTask 收到错误消息类型：" + threadMsg.msgType);
        }
    }

    @Override
    public void init() {
        int len = packet.getLength();
        byte[] data = BU.subByte(packet.getData(), 0, len);
        sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new DataMsgBody(data), MyDef.THREAD_TYPE_PROCESS);
    }

    @Override
    public void destroy() {

    }
}
