package com.zm.MockP2PServer.task;

import com.zm.MockP2PServer.common.MyDef;
import com.zm.MockP2PServer.msg.body.DataMsgBody;
import com.zm.frame.thread.msg.ThreadMsg;
import com.zm.frame.thread.thread.BasicThread;

import static com.zm.frame.log.Log.log;

/**
 * 收发数据异常时，都要结束“收数据线程”和Task
 */
public class LongTcpTask extends TcpTask {
    private Thread thread = null;

    public LongTcpTask(int taskId, BasicThread thread, int time, Object arg) {
        super(taskId, thread, time, arg);
    }

    @Override
    public void processMsg(ThreadMsg threadMsg) {
        if(threadMsg.msgType == MyDef.MSG_TYPE_REPLY) {
            byte[] data = ((DataMsgBody)threadMsg.msgBody).getData();
            //Log.log.debug("收到处理线程消息：" + new String(data));
            if (!send(data)) {
                //发送异常，结束“收数据线程”，关闭链接
                thread.interrupt();//关闭链接后，收数据会异常，“收数据线程”会自己结束，其实不需要
                removeSelfFromThread();
            }
        } else {
            log.error("TCPTask 收到错误消息类型：" + threadMsg.msgType);
        }
    }

    @Override
    public void init() {
        super.init();//先收一次，再后来交给"收数据线程"
        thread = new Thread(new RecThread());
        thread.start();
    }

    //收数据线程
    class RecThread implements Runnable {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                byte[] data;
                if ((data = rec()) != null) {
                    sendThreadMsgTo(MyDef.MSG_TYPE_REQ, new DataMsgBody(data), MyDef.THREAD_TYPE_PROCESS);
                } else { //接受数据异常，"收数据线程"退出
                    break;
                }
            }
        }
    }
}
