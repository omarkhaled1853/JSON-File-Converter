package org.example.protobuffer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.example.MenuProto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProtoBufferConverter {

    public static byte[] convertToProto(String path) {

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(path)));
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONObject menu = jsonObject.getJSONObject("menu");

            if (menu.has("items")) {
                JSONArray items = menu.getJSONArray("items");
                JSONArray filteredItems = new JSONArray();

                for (int i = 0; i < items.length(); i++) {
                    if (!items.isNull(i)) {  // Remove null elements
                        filteredItems.put(items.get(i));
                    }
                }

                menu.put("items", filteredItems);
            }

            // Convert cleaned JSON back to string
            String cleanedJson = jsonObject.toString();

            // Convert JSON to ProtoBuf
            MenuProto.MenuWrapper.Builder menuBuilder = MenuProto.MenuWrapper.newBuilder();
            JsonFormat.parser().merge(cleanedJson, menuBuilder);
            MenuProto.MenuWrapper protoMenu = menuBuilder.build();

            // Serialize to bytes
            return protoMenu.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void writeProto(String path, byte[] protoData) {

        try {
            Files.write(Paths.get(path), protoData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("protoBuffer file created");
    }
}
