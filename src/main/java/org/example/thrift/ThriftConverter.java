package org.example.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ThriftConverter {

    public static byte[] convertToThrift(String path) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(path)));

            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONObject menuJson = jsonObject.getJSONObject("menu");

            // Convert JSON to Thrift objects
            ThriftMenu menu = new ThriftMenu();
            menu.setHeader(menuJson.getString("header"));

            List<MenuItem> itemsList = new ArrayList<>();
            JSONArray itemsJson = menuJson.getJSONArray("items");

            for (int i = 0; i < itemsJson.length(); i++) {
                if (!itemsJson.isNull(i)) { // Ignore null items
                    JSONObject itemJson = itemsJson.getJSONObject(i);
                    MenuItem menuItem = new MenuItem();
                    menuItem.setId(itemJson.getString("id"));
                    if (itemJson.has("label")) {
                        menuItem.setLabel(itemJson.getString("label"));
                    }
                    itemsList.add(menuItem);
                }
            }

            menu.setItems(itemsList);
            MenuWrapper menuWrapper = new MenuWrapper();
            menuWrapper.setMenu(menu);

            // Serialize to binary file
            TSerializer serializer = new TSerializer(new TBinaryProtocol.Factory());

            return serializer.serialize(menuWrapper);
        } catch (TException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeThrift(String path, byte[] thriftData) {

        try {
            Files.write(Paths.get(path), thriftData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Thrift file created");
    }
}
