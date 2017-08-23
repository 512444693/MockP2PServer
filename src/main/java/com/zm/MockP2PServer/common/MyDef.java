package com.zm.MockP2PServer.common;

import java.io.File;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class MyDef {

    // 存放收发包的目录
    public static final String MOCK_FILE_PATH = "mock" + File.separator;

    //每次收包大小
    public static final int MAX_PACKET_SIZE = 4096;

    // thread type
    public static final int THREAD_TYPE_LISTEN_UDP = 1001;
    public static final int THREAD_TYPE_LISTEN_TCP = 1002;
    public static final int THREAD_TYPE_REC_AND_SEND = 1003;
    public static final int THREAD_TYPE_LONG_TCP_REC_AND_SEND = 1004;
    public static final int THREAD_TYPE_PROCESS = 1005;
    public static final int THREAD_TYPE_ON_TIME = 1006;

    // message type
    public static final int MSG_TYPE_UDP_CNT = 2001;
    public static final int MSG_TYPE_TCP_CNT = 2002;
    public static final int MSG_TYPE_REQ = 2003;
    public static final int MSG_TYPE_REPLY = 2004;

    // task type
    public static final int TASK_TYPE_UDP = 3001;
    public static final int TASK_TYPE_TCP = 3002;
    public static final int TASK_TYPE_LONG_TCP = 3003;
}