package org.example;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonFile {

    public JSONObject readJson(String path) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(path));
            return (JSONObject) obj;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
