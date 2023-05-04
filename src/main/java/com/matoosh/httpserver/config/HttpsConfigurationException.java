package com.matoosh.httpserver.config;

public class HttpsConfigurationException extends RuntimeException {

    public HttpsConfigurationException(String message) {
        super(message);
    }

    public HttpsConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpsConfigurationException(Throwable cause) {
        super(cause);
    }
}