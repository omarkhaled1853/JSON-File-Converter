package org.example.avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AvroConverter {

    private static Schema schema;

    public static void readAvroSchema(String path) {
        try {
            schema = new Schema.Parser().parse(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GenericRecord convertToAvro(String path) {

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(path)));
            // Convert JSON to Avro
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonContent).get("menu");

            // Create Avro record
            GenericRecord menuRecord = new GenericData.Record(schema);
            menuRecord.put("header", rootNode.get("header").asText());

            // Process items
            List<GenericRecord> itemsList = new ArrayList<>();
            Schema itemSchema = schema.getField("items").schema().getElementType();

            for (JsonNode item : rootNode.get("items")) {
                if (item.isNull()) continue; // Skip null items

                GenericRecord itemRecord = new GenericData.Record(itemSchema);
                itemRecord.put("id", item.get("id").asText());

                // Handle optional label
                if (item.has("label")) {
                    itemRecord.put("label", item.get("label").asText());
                } else {
                    itemRecord.put("label", null);
                }

                itemsList.add(itemRecord);
            }

            menuRecord.put("items", itemsList);

            return menuRecord;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeAvro(GenericRecord menuRecord, String path) {
        File avroFile = new File(path);
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter);

        try {
            dataFileWriter.create(schema, avroFile);
            dataFileWriter.append(menuRecord);
            dataFileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Avro file created");
    }
}
