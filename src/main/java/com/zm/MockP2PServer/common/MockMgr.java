package com.zm.MockP2PServer.common;

import com.zm.MockP2PServer.server.MockP2PServer;
import static com.zm.frame.log.Log.log;

import java.io.File;

/**
 * Created by zhangmin on 2017/8/24.
 */
public class MockMgr {
    private String mockeFilePath = MyDef.MOCK_FILE_PATH;
    //private boolean matchAll = MockP2PServer.getInstance().getConfig().isMatchAll();

    public byte[] getResponse(byte[] data) {
        return null;
    }

    public void readMockFileFromDir() {
        File dir = new File(mockeFilePath);
        if (!dir.isDirectory()) {
            log.error(mockeFilePath + " not exist or is not a directory");
            return;
        }
        File[] files = dir.listFiles();
        /*if ((files.length % 2) != 0) {
            log.error("The num of mock file is not right, it should be even");
            return;
        }*/
        for(File file : files) {
            if (!file.isFile()) {
                log.error(file.getName() + " is not a file");
                return;
            }
            log.debug(file.getName());
        }
    }
    public static void main(String[] args) {
        new MockMgr().readMockFileFromDir();
    }
}