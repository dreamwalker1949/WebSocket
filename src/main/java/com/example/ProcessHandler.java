package com.example;

import com.zaxxer.nuprocess.NuAbstractProcessHandler;
import com.zaxxer.nuprocess.NuProcess;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

class ProcessHandler extends NuAbstractProcessHandler {

    private NuProcess nuProcess;
    private Session session;

    public ProcessHandler(Session session) {
        this. session = session;
    }

    @Override
    public void onStart(NuProcess nuProcess) {
        this.nuProcess = nuProcess;
    }

  @Override
    public boolean onStdinReady(ByteBuffer buffer) {
        buffer.put("Hello world!".getBytes());
        buffer.flip();
        return false; // false means we have nothing else to write at this time
    }

    @Override
    public void onStdout(ByteBuffer buffer, boolean closed) {
        if (!closed) {
            byte[] bytes = new byte[buffer.remaining()];
            // You must update buffer.position() before returning (either implicitly,
            // like this, or explicitly) to indicate how many bytes your handler has consumed.
            buffer.get(bytes);
            try {
                session.getBasicRemote().sendText(new String(bytes, Charset.forName("gbk")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // For this example, we're done, so closing STDIN will cause the "cat" process to exit
            nuProcess.closeStdin(true);
        }
    }

}