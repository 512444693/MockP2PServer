package com.zm.MockP2PServer.msg.body;

import com.zm.frame.thread.msg.ThreadMsgBody;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class UdpMsgBody extends ThreadMsgBody {
    private DatagramSocket socket;
    private DatagramPacket packet;

    public DatagramPacket getPacket() {
        return packet;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public UdpMsgBody(DatagramPacket datagramPacket, DatagramSocket datagramSocket) {
        this.packet = datagramPacket;
        this.socket = datagramSocket;
    }
}
