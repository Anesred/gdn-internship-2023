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

    //Loading Configuration file by the path provided
    public void loadConfigurationFile(String filePath) {
        FileReader fileReader;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpsConfigurationException(e);
        }
        StringBuilder sb = new StringBuilder();
        int i;
        while (true) {
            try {
                if ((i = fileReader.read()) == -1) break;
            } catch (IOException e) {
                throw new HttpsConfigurationException(e);
            }
            sb.append((char)i);
        }
        JsonNode conf;
        try {
            conf = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new HttpsConfigurationException("Error Parsing The Configuration File.", e);
        }
        try {
            myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpsConfigurationException("Error Parsing The Configuration File, internal",e);
        }
    }


    //Returns the current loaded configuration
    public Configuration getCurrentConfiguration() {

        if(myCurrentConfiguration == null) {
            throw new HttpsConfigurationException("No Current Configuration Set.");
        }

        return myCurrentConfiguration;

    }

}
