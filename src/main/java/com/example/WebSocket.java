package com.example;

import com.zaxxer.nuprocess.NuProcess;
import com.zaxxer.nuprocess.NuProcessBuilder;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

//客户端可以通过这个URI来连接到WebSocket。
@ServerEndpoint("/websocket")
public class WebSocket {

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        NuProcessBuilder pb = new NuProcessBuilder(Collections.singletonList(message));
        pb.setProcessListener(new ProcessHandler(session));
        pb.start();
    }

}
