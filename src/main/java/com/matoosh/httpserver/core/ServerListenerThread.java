package com.matoosh.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread{
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    private int port;
    private String webRoot;
    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webRoot) throws IOException {
        this.port = port;
        this.webRoot = webRoot;
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        connect();
    }

    private void connect() {
        try {
            startThread();
        } catch (IOException e) {
            LOGGER.error("Problem with setting socket", e);
        } finally {
            closeConnection();
        }
    }

    private void startThread() throws IOException {
        while ( serverSocket.isBound() && !serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();

            LOGGER.info(" * Connection accepted: " + socket.getInetAddress());

            HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
            workerThread.start();
        }
    }

    private void closeConnection() {
        if (serverSocket!=null) {
            try {
                serverSocket.close();
            } catch (IOException e) {}
        }
    }
}
