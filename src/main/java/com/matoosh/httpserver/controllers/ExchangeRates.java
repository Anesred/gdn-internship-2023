package com.matoosh.httpserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.matoosh.http.HttpParsingException;
import com.matoosh.http.HttpStatusCode;
import com.matoosh.httpserver.model.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExchangeRates {
    private final static Logger LOGGER = LoggerFactory.getLogger(ExchangeRates.class);

    private String makeApiRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                return content.toString();
            } finally {
                connection.disconnect();
            }
        } else {
            throw new IOException("Error: " + responseCode);
        }
    }

    public String getAverageExchangeRate(String apiUrl, String[] pathParts) throws HttpParsingException, IOException {
        if (pathParts.length == 4) {
            apiUrl += "A/" + pathParts[2] + "/" + pathParts[3] + "/";
        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        LOGGER.debug("Sending request to endpoint: " + apiUrl);
        JsonNode rootNode = Json.parse(makeApiRequest(apiUrl));
        return formatAverageDateResults(rootNode);
    }

    public String getMinMaxAverageValue(String apiUrl, String[] pathParts) throws HttpParsingException, IOException {
        if (pathParts.length == 5 && "last".equals(pathParts[3]) && Integer.parseInt(pathParts[4]) <= 255 && Integer.parseInt(pathParts[4]) >= 0) {
            apiUrl += "A/" + pathParts[2] + "/last/" + pathParts[4] + "/";
        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        LOGGER.debug("Sending request to endpoint: " + apiUrl);
        JsonNode rootNode = Json.parse(makeApiRequest(apiUrl));
        return formatMinMaxAverageResults(rootNode);
    }

    public String getMajorDifference(String apiUrl, String[] pathParts) throws HttpParsingException, IOException {
        if (pathParts.length == 5 && "last".equals(pathParts[3]) && Integer.parseInt(pathParts[4]) <= 255 && Integer.parseInt(pathParts[4]) >= 0) {
            apiUrl += "C/" + pathParts[2] + "/last/" + pathParts[4] + "/";
        } else {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        LOGGER.debug("Sending request to endpoint: " + apiUrl);
        JsonNode rootNode = Json.parse(makeApiRequest(apiUrl));
        return formatBiggestDifferenceResults(rootNode);
    }

    private String formatAverageDateResults(JsonNode rootNode) {
        String currency = rootNode.path("currency").asText();
        String code = rootNode.path("code").asText();
        String date = rootNode.path("rates").get(0).path("effectiveDate").asText();
        String mid = rootNode.path("rates").get(0).path("mid").asText();

        return "Currency: " + currency + "\nCode: " + code + "\nDate: " + date + "\nValue: " + mid;
    }

    private String formatMinMaxAverageResults(JsonNode rootNode) {
        double min = rootNode.path("rates").get(0).path("mid").asDouble();
        String minDate = rootNode.path("rates").get(0).path("effectiveDate").asText();
        double max = rootNode.path("rates").get(0).path("mid").asDouble();
        String maxDate = rootNode.path("rates").get(0).path("effectiveDate").asText();
        for (int i = 0; i < rootNode.path("rates").size(); i++) {
            if (rootNode.path("rates").get(i).path("mid").asDouble() < min) {

                min = rootNode.path("rates").get(i).path("mid").asDouble();
                minDate = rootNode.path("rates").get(i).path("effectiveDate").asText();
            } else if (rootNode.path("rates").get(i).path("mid").asDouble() > max) {

                max = rootNode.path("rates").get(i).path("mid").asDouble();
                maxDate = rootNode.path("rates").get(i).path("effectiveDate").asText();
            }
        }
        String currency = rootNode.path("currency").asText();
        String code = rootNode.path("code").asText();

        return "Currency: " + currency + "\nCode: " + code + "\nMin Date: " + minDate + "\nMin Value: " + min
                + "\nMax Date: " + maxDate + "\nMax Value: " + max;
    }


    private String formatBiggestDifferenceResults(JsonNode rootNode) {
        double bid = rootNode.path("rates").get(0).path("bid").asDouble();
        String date = rootNode.path("rates").get(0).path("effectiveDate").asText();
        double ask = rootNode.path("rates").get(0).path("ask").asDouble();
        double difference = ask - bid;
        for (int i = 0; i < rootNode.path("rates").size(); i++) {
            if ((rootNode.path("rates").get(i).path("ask").asDouble() - rootNode.path("rates").get(i).path("bid").asDouble()) > difference) {

                bid = rootNode.path("rates").get(i).path("bid").asDouble();
                ask = rootNode.path("rates").get(i).path("ask").asDouble();
                date = rootNode.path("rates").get(i).path("effectiveDate").asText();
                difference = ask - bid;

            }
        }
        String currency = rootNode.path("currency").asText();
        String code = rootNode.path("code").asText();

        return "Major difference\nCurrency: " + currency + "\nCode: " + code + "\nDate: " + date + "\nBid Value: " + bid
                + "\nAsk Value: " + ask + "\nDifference: " + difference;
    }
}
