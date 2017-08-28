package com.zm.MockP2PServer.common;

import com.zm.MockP2PServer.command.MockFile;
import com.zm.MockP2PServer.server.MockP2PServer;
import com.zm.message.Message;
import com.zm.message.RequestMessage;

import static com.zm.frame.log.Log.log;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zhangmin on 2017/8/24.
 */
public class MockMgr {
    private String mockFilePath = D.MOCK_FILE_PATH;
    private boolean matchAll = MockP2PServer.getInstance().getConfig().isMatchAll();
    private Map<String, MockFile> mockMap = new HashMap();

    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private Lock readLock = rwLock.readLock();
    private Lock writeLock = rwLock.writeLock();

    //存放尝试解码异常，打印log使用
    private Exception e = null;

    private static final MockMgr instance = new MockMgr();

    private MockMgr() {
        readMockFileFromDir();
    }

    public static MockMgr getInstance() {
        return instance;
    }

    public byte[] getResponse(byte[] data) {
        readLock.lock();
        byte[] respData = null;
        if (mockMap.isEmpty()) {
            log.error("没有MockFile可用");
            readLock.unlock();
            return respData;
        }
        MockFile mockFile;
        Message recMsg = null;
        for (Map.Entry<String, MockFile> entry : mockMap.entrySet()) {
            mockFile = entry.getValue();
            recMsg = tryDecode(mockFile.getReq().getP2pStr(), data);
            if ((!matchAll && recMsg.getCmdID() == mockFile.getCmdId()) ||
                    (matchAll && mockFile.matchAll(recMsg))) {
                RequestMessage.registerAsReqMsg(recMsg);
                respData = mockFile.getResp().getData();
                log.info("收到******************" + entry.getKey() + "******************：\r\n" +
                        (e == null ? recMsg.toString() : e.getMessage()));
                if (recMsg.dataCntLeftToDecode() > 0) {
                    log.error("还剩" + recMsg.dataCntLeftToDecode() + "字节数据没有解码\r\n");
                }
                log.debug("回包 " + entry.getKey() + " ：\r\n" + mockFile.getResp().getMessageStr());
                RequestMessage.clearReqMsg();
                readLock.unlock();
                return respData;
            }
        }
        log.error("没有找到匹配的回包，收到的请求包的内容可能是：\r\n" + recMsg.toString());
        readLock.unlock();
        return respData;
    }

    public Message tryDecode(String p2pStr, byte[] data) {
        //每次尝试解码都将异常置为null
        this.e = null;
        Message msg = null;
        try {
            msg = new Message(p2pStr, data);
            msg.decode();
            return msg;
        } catch (Exception e) {
            this.e = e;
        }
        return msg;
    }

    public void readMockFileFromDir() {
        File dir = new File(mockFilePath);
        if (!dir.isDirectory()) {
            log.error(mockFilePath + " not exist or is not a directory");
            return;
        }
        File[] files = dir.listFiles();

        writeLock.lock();
        //记录需要删除的文件名
        Set<String> removeSet = new HashSet<>(mockMap.keySet());
        for(File file : files) {
            if (!file.isFile() || file.isHidden()) {
                //log.debug(file.getName() + " is not a file or is a hidden file");
                continue;
            }
            removeSet.remove(file.getName());
            try {
                if (mockMap.containsKey(file.getName())) {
                    //更新
                    MockFile mockFile = mockMap.get(file.getName());
                    if (file.lastModified() > mockFile.getLastModified()) {
                        mockFile.update(readFile(file), file.lastModified());
                    }
                } else {
                    //添加
                    mockMap.put(file.getName(), new MockFile(readFile(file), file.lastModified()));
                }
            } catch (Exception e) {
                log.error(file.getName() + "错误："  + e.getMessage());
            }
        }

        //删除
        for (String fileName : removeSet) {
            mockMap.remove(fileName);
        }
        writeLock.unlock();
    }

    private String readFile(File file) {
        String ret = "";
        BufferedReader in = null;
        try {
             in = new BufferedReader(new FileReader(file));
            char[] data = new char[4096];
            int len;
            StringBuffer sb = new StringBuffer();
            while((len = in.read(data)) != -1) {
                sb.append(data, 0, len);
            }
            ret = sb.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("关闭文件异常：" + e.getMessage());
                }
            }
        }
        return ret;
    }
}