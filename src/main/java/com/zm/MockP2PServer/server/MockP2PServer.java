package com.zm.MockP2PServer.server;

import com.zm.MockP2PServer.common.ConnectionType;
import com.zm.MockP2PServer.common.MyClassFactory;
import com.zm.MockP2PServer.common.MyConfig;
import com.zm.MockP2PServer.common.MyDef;
import com.zm.frame.thread.server.ThreadServer;
import com.zm.frame.thread.thread.MyThreadGroup;

import static com.zm.frame.log.Log.log;

/**
 * Created by zhangmin on 2017/8/23.
 */
public class MockP2PServer {
    private static final MockP2PServer instance = new MockP2PServer();

    private MyConfig config;

    private MockP2PServer(){}

    public void init(String[] args) {

        // init config
        config = new MyConfig();
        String cntTypeStr = "", portStr = "";
        if(args.length == 2) {
            cntTypeStr = args[0];
            portStr = args[1];
        } else if (args.length == 1) {
            cntTypeStr = "-t";
            portStr = args[0];
        } else {
            exit();
        }
        switch (cntTypeStr) {
            case "-l" : config.setCntType(ConnectionType.LONG_TCP); break;
            case "-t" : config.setCntType(ConnectionType.TCP); break;
            case "-u" : config.setCntType(ConnectionType.UDP); break;
            default:
                log.error("Wrong connection type : " + cntTypeStr);
                exit();
        }
        try {
            config.setPort(Integer.parseInt(portStr));
        } catch (Exception e) {
            log.error("Wrong port : " + portStr);
            exit();
        }
        log.debug("Init ok, " + config.getCntType() + "/" + config.getPort());

        // init thread
        new MyClassFactory();
        if(config.getCntType() == ConnectionType.UDP) {
            new MyThreadGroup(MyDef.THREAD_TYPE_LISTEN_UDP, 1, config.getPort());
        } else {
            new MyThreadGroup(MyDef.THREAD_TYPE_LISTEN_TCP, 1, config.getPort());
        }
        new MyThreadGroup(MyDef.THREAD_TYPE_REC_AND_SEND, 1, null);
        new MyThreadGroup(MyDef.THREAD_TYPE_PROCESS, 1, null);

        //100ms处理一次
        new MyThreadGroup(MyDef.THREAD_TYPE_ON_TIME, 1, 100);
    }

    public void exit() {
        log.debug(
                "Usage:\r\n" +
                "java MockP2PServer.jar [-t, -u, -l] port\r\n" +
                "-t, TCP default\r\n" +
                "-u, UDP \r\n" +
                "-l, 长连接"
        );
        System.exit(-1);
    }

    public static MockP2PServer getInstance() {
        return instance;
    }

    public MyConfig getConfig() {
        return config;
    }

    public void start(String[] args) {
        init(args);
        ThreadServer.getInstance().startThreads();
    }
}
