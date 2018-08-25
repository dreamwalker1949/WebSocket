package com.example;

import com.zaxxer.nuprocess.NuProcess;
import com.zaxxer.nuprocess.NuProcessBuilder;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。
@ServerEndpoint("/websocket")
public class WebSocket {

    //concurrent包的线程安全Set，用来存放每个客户端对应yWebSocket对象。
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        webSocketSet.add(this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            String msg = message + "not a legal input";
            try {
                NuProcessBuilder pb = new NuProcessBuilder(Arrays.asList(message));
                ProcessHandler handler = new ProcessHandler();
                pb.setProcessListener(handler);
                NuProcess process = pb.start();
                process.waitFor(0, TimeUnit.SECONDS);
                msg = handler.getMsg();
            } catch (Exception e) {
                e.printStackTrace();
            }
            session.getBasicRemote().sendText(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
