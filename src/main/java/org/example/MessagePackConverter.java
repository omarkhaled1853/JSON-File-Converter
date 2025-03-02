package org.example;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MessagePackConverter {

    public byte[] convertToMessagePack(Map<String, Object> data) {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        try {
            pack(packer, data);
            packer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return packer.toByteArray();
    }

    private void pack(MessageBufferPacker packer, Object value) throws IOException {
        if (value == null) {

            packer.packNil();
        } else if (value instanceof String) {

            packer.packString((String) value);
        } else if (value instanceof Integer) {

            packer.packInt((Integer) value);
        } else if (value instanceof Long) {

            packer.packLong((Long) value);
        } else if (value instanceof Double) {

            packer.packDouble((Double) value);
        } else if (value instanceof Boolean) {

            packer.packBoolean((Boolean) value);
        } else if (value instanceof List) {

            List<?> list = (List<?>) value;
            packer.packArrayHeader(list.size());
            for (Object item : list) {
                pack(packer, item);
            }
        } else if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            packer.packMapHeader(map.size());
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                pack(packer, entry.getKey());
                pack(packer, entry.getValue());
            }
        } else {
            throw new IllegalArgumentException("Unsupported type: " + value.getClass());
        }
    }
}
