package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.MyDef;
import com.zm.MockP2PServer.msg.body.UdpMsgBody;
import com.zm.frame.thread.thread.BasicThread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static com.zm.frame.log.Log.log;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class UdpListenThreadImpl extends BasicThread {
    private int port;


    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public UdpListenThreadImpl(int threadType, int threadId, Object arg) {
        super(threadType, threadId);
        this.port = (int) arg;
    }

    @Override
    protected void init() {}

    @Override
    protected void process() {
        try {
            datagramSocket = new DatagramSocket(port);
            while(true) {
                datagramPacket = new DatagramPacket(new byte[4096], 4096);
                datagramSocket.receive(datagramPacket);
                sendThreadMsgTo(MyDef.MSG_TYPE_UDP_CNT, new UdpMsgBody(
                        datagramPacket, datagramSocket), MyDef.THREAD_TYPE_REC_AND_SEND);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }
}
