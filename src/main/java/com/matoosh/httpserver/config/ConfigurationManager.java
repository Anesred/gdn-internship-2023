package com.matoosh.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.matoosh.httpserver.model.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager() {

    }

    public static ConfigurationManager getInstance() {
        if(myConfigurationManager==null)
            myConfigurationManager = new ConfigurationManager();
        return myConfigurationManager;
    }

    public void loadConfigurationFile(String filePath) {
        FileReader fileReader;
        fileReader = getFileReader(filePath);
        StringBuilder sb = new StringBuilder();
        appendConfigurationChar(fileReader, sb);
        JsonNode conf;
        conf = getJsonNode(sb);
        getCurrentConfiguration(conf);
    }

    private FileReader getFileReader(String filePath) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpsConfigurationException(e);
        }
        return fileReader;
    }

    private void appendConfigurationChar(FileReader fileReader, StringBuilder sb) {
        int i;
        while (true) {
            try {
                if ((i = fileReader.read()) == -1) break;
            } catch (IOException e) {
                throw new HttpsConfigurationException(e);
            }
            sb.append((char)i);
        }
    }

    private JsonNode getJsonNode(StringBuilder sb) {
        JsonNode conf;
        try {
            conf = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new HttpsConfigurationException("Error Parsing The Configuration File.", e);
        }
        return conf;
    }

    private void getCurrentConfiguration(JsonNode conf) {
        try {
            myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpsConfigurationException("Error Parsing The Configuration File, internal",e);
        }
    }

    public Configuration getCurrentConfiguration() {

        if(myCurrentConfiguration == null) {
            throw new HttpsConfigurationException("No Current Configuration Set.");
        }

        return myCurrentConfiguration;

    }

}
