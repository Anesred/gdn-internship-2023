package com.matoosh.httpserver.controllers;

import com.matoosh.http.HttpParser;
import com.matoosh.http.HttpParsingException;
import com.matoosh.http.HttpRequest;
import com.matoosh.http.HttpStatusCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamHandler {

    public void generateResponse(InputStream inputStream, OutputStream outputStream) throws IOException, HttpParsingException {
        HttpParser httpParser = new HttpParser();
        HttpRequest httpRequest = httpParser.parseHttpRequest(inputStream);

        String requestLine = httpRequest.getMethod() + " " + httpRequest.getRequestTarget() + " " + httpRequest.getOriginalHttpVersion();

        String[] requestParts = requestLine.split(" ");
        if (requestParts.length < 2) {
            return;
        }
        String requestPath = requestParts[1];

        String apiResponse = "";
        if (requestPath.startsWith("/average/") || requestPath.startsWith("/minmaxaverage/") || requestPath.startsWith("/majordifference/")) {

            String apiUrl = "http://api.nbp.pl/api/exchangerates/rates/";
            ExchangeRates exchangeRates = new ExchangeRates();
            String[] pathParts = requestPath.split("/");

            if(requestPath.startsWith("/average/")) {
                apiResponse = exchangeRates.getAverageExchangeRate(apiUrl, pathParts);
            } else if (requestPath.startsWith("/minmaxaverage/")) {
                apiResponse = exchangeRates.getMinMaxAverageValue(apiUrl, pathParts);
            } else if (requestPath.startsWith("/majordifference/")) {
                apiResponse = exchangeRates.getMajorDifference(apiUrl, pathParts);
            }

        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND);
        }

        final String CRLF = "\r\n";
        String response = "HTTP/1.1 200 OK" + CRLF +
                "Content-Length: " + apiResponse.getBytes().length + CRLF +
                CRLF +
                apiResponse +
                CRLF + CRLF;

        outputStream.write(response.getBytes());
    }

}
