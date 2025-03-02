package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;



public class Main {
    public static void main(String[] args) {

//        Read JSON file & convert to MessagePack file
        String inputPath = "myJson.json";

        Map<String, Object> jsonMap = JsonFile.readJson(inputPath);

        // Convert Map to MessagePack
        byte[] msgPackData = MessagePackConverter.convertToMessagePack(jsonMap);

        String outputPath = "myMsgPack.msgpack";
        // Save MessagePack to file
        MessagePackConverter.writeMessagePack(outputPath, msgPackData);
//        ============================================================================================

//        Read MessagePack file & convert back to JSON (verification)
        try {
            byte[] readMsgPack = Files.readAllBytes(Paths.get("myMsgPack.msgpack"));
            Object decodedObject = MessagePackConverter.convertFromMessagePack(readMsgPack);

            ObjectMapper objectMapper = new ObjectMapper();
            String decodedJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(decodedObject);
            System.out.println("Decoded JSON:\n" + decodedJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}