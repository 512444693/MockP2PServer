package com.zm.MockP2PServer.thread;

import com.zm.MockP2PServer.common.ConnectionType;
import com.zm.MockP2PServer.common.MyDef;
import com.zm.MockP2PServer.msg.body.UdpMsgBody;
import com.zm.MockP2PServer.server.MockP2PServer;
import com.zm.frame.thread.thread.BasicThread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import static com.zm.frame.log.Log.log;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class ListenThreadImpl extends BasicThread {
    private ConnectionType cntType;
    private int port;

    private ServerSocket serverSocket;
    private Socket socket;

    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public ListenThreadImpl(int threadType, int threadId) {
        super(threadType, threadId);
    }

    @Override
    protected void init() {
        cntType = MockP2PServer.getInstance().getConfig().getCntType();
        port = MockP2PServer.getInstance().getConfig().getPort();
    }

    @Override
    protected void process() {
        try {
            if(cntType == ConnectionType.UDP) {
                datagramSocket = new DatagramSocket(port);
                while(true) {
                    datagramPacket = new DatagramPacket(new byte[4096], 4096);
                    datagramSocket.receive(datagramPacket);
                    sendThreadMsgTo(MyDef.MSG_TYPE_UDP_CNT, new UdpMsgBody(datagramPacket), MyDef.THREAD_TYPE_REC_AND_SEND);
                }
            } else {
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            System.exit(-1);
        }
    }
}
