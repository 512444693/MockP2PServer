package com.zm.MockP2PServer.task;

import com.zm.MockP2PServer.common.MyDef;
import com.zm.MockP2PServer.msg.body.DataMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.thread.BasicThread;
import com.zm.utils.BU;

import static com.zm.frame.log.Log.log;

/**
 * 收发数据异常时，都要结束“收数据线程”和Task
 */
public class LongTcpTask extends TcpTask {
    private Thread thread = null;
    private static final String pingInCommonStr =
            "000000000000000000000000000000000000ffffffffffffffffffffffff00000000";
    //某种长连接的ping值
    private static final String pingValueStr = "ffff" + pingInCommonStr;
    //该长连接ping回包
    private static final byte[] pingRetValue = BU.hex2Bytes("fffe" + pingInCommonStr);

    public LongTcpTask(int taskId, BasicThread thread, int time, Object arg) {
        super(taskId, thread, time, arg);
    }

    @Override
    public void processMsg(ThreadMsg threadMsg) {
        if(threadMsg.msgType == MyDef.MSG_TYPE_REPLY) {
            byte[] data = ((DataMsgBody)threadMsg.msgBody).getData();
            //Log.log.debug("收到处理线程消息：" + new String(data));
            if (!send(data)) {
                //发送异常，关闭链接
                removeSelfFromThread();
            }
        } else {
            log.error("TCPTask 收到错误消息类型：" + threadMsg.msgType);
        }
    }

    @Override
    public void init() {
        log.debug("链接 " + socket.getInetAddress().getHostAddress() +
                ":" + socket.getPort());
        thread = new Thread(new RecThread());
        thread.start();
    }

    @Override
    public void destroy() {
        super.destroy();
        thread.interrupt();//关闭链接后，收数据会异常，“收数据线程”会自己结束，其实不需要interrupt()
    }

    //收数据线程
    class RecThread implements Runnable {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                byte[] data;
                if ((data = rec()) != null) {
                    if (BU.bytes2Hex(data).matches("^0{92}$")){// 长连接ping
                        send(data);
                    } else if (BU.bytes2Hex(data).equals(pingValueStr)) {// 另一种长连接ping
                        send(pingRetValue);
                    } else {
                        sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new DataMsgBody(data), MyDef.THREAD_TYPE_PROCESS);
                    }
                } else { //接受数据异常，"收数据线程"退出
                    break;
                }
            }
        }
    }
}
