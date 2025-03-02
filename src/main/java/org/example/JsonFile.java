package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFile {

    public Map<String, Object> readJson(String path) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(path)));
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(jsonContent, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
