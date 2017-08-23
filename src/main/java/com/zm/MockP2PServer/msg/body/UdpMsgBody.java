package com.zm.MockP2PServer.msg.body;

import com.zm.frame.thread.msg.ThreadMsgBody;

import java.net.DatagramPacket;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class UdpMsgBody extends ThreadMsgBody {
    private DatagramPacket packet;

    public DatagramPacket getPacket() {
        return packet;
    }

    public void setPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    public UdpMsgBody(DatagramPacket datagramPacket) {
        this.packet = datagramPacket;
    }
}
