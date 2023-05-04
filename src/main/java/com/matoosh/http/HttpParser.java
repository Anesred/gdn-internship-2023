package com.matoosh.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private  final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13  Carriage Return
    private static final int LF = 0x0A; // 10  Line Feed

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request)
            throws IOException, HttpParsingException {
        StringBuilder dataBuffer = new StringBuilder();
        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        int _byte;

        while ((_byte = reader.read()) >= 0) {
            if (_byte == CR) {
                processCarriageReturn(reader, request, dataBuffer, methodParsed, requestTargetParsed);
                return;
            }

            if (_byte == SP) {
                if (!methodParsed){
                    methodParsed = parseMethod(request, dataBuffer);
                } else if (!requestTargetParsed) {
                    requestTargetParsed = parseRequestTarget(request, dataBuffer);
                } else {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                dataBuffer.delete(0, dataBuffer.length());

            } else {
                dataBuffer.append((char) _byte);

                if (!methodParsed) {
                    if (dataBuffer.length() > HttpMethod.MAX_LENGTH) {
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }

    }

    private boolean parseRequestTarget(HttpRequest request, StringBuilder dataBuffer) throws HttpParsingException {
        boolean requestTargetParsed;
        LOGGER.debug("Request Line REQ TARGET to Process : {}", dataBuffer);
        request.setRequestTarget(dataBuffer.toString());
        requestTargetParsed = true;
        return requestTargetParsed;
    }

    private boolean parseMethod(HttpRequest request, StringBuilder dataBuffer) throws HttpParsingException {
        boolean methodParsed;
        LOGGER.debug("Request Line METHOD to Process : {}", dataBuffer);
        request.setMethod(dataBuffer.toString());
        methodParsed = true;
        return methodParsed;
    }

    private void processCarriageReturn(InputStreamReader reader, HttpRequest request, StringBuilder processingDataBuffer, boolean methodParsed, boolean requestTargetParsed) throws IOException, HttpParsingException {
        int _byte;
        _byte = reader.read();
        if (_byte == LF) {

            LOGGER.debug("Request Line VERSION to Process : {}", processingDataBuffer);
            if (!methodParsed || !requestTargetParsed) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            }

            try {
                request.setHttpVersion(processingDataBuffer.toString());
            } catch (BadHttpVersionException e) {
                throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            }

            return;
        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

}
