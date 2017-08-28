package com.zm.MockP2PServer.command;

import com.zm.message.Message;

/**
 * Created by zhangmin on 2017/8/25.
 */
public class MockFile extends BaseMock{
    private ReqMock req;
    private RespMock resp;

    public MockFile(String p2pStr, long lastModified) {
        super(p2pStr, lastModified);
        String[] strings = p2pStr.split("\\-{3,}");
        if (strings.length == 2) {
            this.req = new ReqMock(strings[0], lastModified);
            this.resp = new RespMock(strings[1], lastModified);
        } else {
            throw new IllegalArgumentException("创建MockFile失败，缺少请求包或回包");
        }
    }

    protected void update(String p2pStr) {
        //log.debug("Mock file updated");
        String[] strings = p2pStr.split("\\-{3,}");
        if (strings.length == 2) {
            this.req.update(strings[0], this.getLastModified());
            this.resp.update(strings[1], this.getLastModified());
        } else {
            throw new IllegalArgumentException("更新MockFile失败，缺少请求包或回包");
        }
    }

    public ReqMock getReq() {
        return req;
    }

    public RespMock getResp() {
        return resp;
    }

    public int getCmdId() {
        return req.getCmdId();
    }

    public boolean matchAll(Message recMsg) {
        return this.req.getMessage().compare(recMsg).equal;
    }
}
