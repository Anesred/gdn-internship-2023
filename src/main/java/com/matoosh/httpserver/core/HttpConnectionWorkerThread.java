package com.matoosh.httpserver.core;

import com.matoosh.http.HttpParsingException;
import com.matoosh.httpserver.controllers.InputStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            InputStreamHandler inputStreamHandler = new InputStreamHandler();
            inputStreamHandler.generateResponse(inputStream, outputStream);

            LOGGER.info(" * Processing Finished");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } catch (HttpParsingException e) {
            LOGGER.error("Problem with generating response", e);
        }
    }

}
