## 特点
1. 支持UDP，TCP和TCP长连接
2. 支持[完全匹配]和[只匹配cmdid](修改conf.properties)
3. 支持多线程
4. 支持动态更改mock文件
5. 支持更改日志级别(修改conf.properties)

## mock文件
1. 收包和回包之间用至少3个“-”区别
2. 可写多个mock文件保存到mock目录中，根据cmdid或完全匹配来回包
3. 更改后不需要重启服务器，最多10s后服务器可自动加载

```
格式示例：
[h]
4@ProtocolVer = *
4@SequenceNum = *
4@BodyLen = *
1@CmdId = 60

[b]
h@ChannelID = *

---

[h]
4@ProtocolVer = *
4@SequenceNum = *
4@BodyLen = *
1@CmdId = 61

[b]
1@result = 0
h@ChannelID = super.ChannelID
```

## 使用
```
java MockP2PServer-1.0.0.jar [-t, -u, -l] port
-t, TCP 默认，可以不写
-u, UDP
-l, 长连接
```