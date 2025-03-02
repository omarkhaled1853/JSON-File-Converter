package org.example;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class MessagePackConverter {

    public static byte[] convertToMessagePack(Map<String, Object> data) {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

        try {
            pack(packer, data);
            packer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return packer.toByteArray();
    }

    public static Object convertFromMessagePack(byte[] msgPackData) {
        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(msgPackData);
        try {
            return unpack(unpacker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void writeMessagePack(String path, byte[] msgPackData) {

        try {
            Files.write(Paths.get(path), msgPackData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("MessagePack file created: output.msgpack");
    }

    private static void pack(MessageBufferPacker packer, Object value) throws IOException {
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

    // Recursive unpacking function
    private static Object unpack(MessageUnpacker unpacker) throws IOException {
        switch (unpacker.getNextFormat().getValueType()) {
            case NIL:
                unpacker.unpackNil();
                return null;
            case BOOLEAN:
                return unpacker.unpackBoolean();
            case INTEGER:
                return unpacker.unpackLong();
            case FLOAT:
                return unpacker.unpackDouble();
            case STRING:
                return unpacker.unpackString();
            case BINARY:
                return unpacker.readPayload(unpacker.unpackBinaryHeader());
            case ARRAY:
                int arraySize = unpacker.unpackArrayHeader();
                Object[] array = new Object[arraySize];
                for (int i = 0; i < arraySize; i++) {
                    array[i] = unpack(unpacker);
                }
                return java.util.Arrays.asList(array);
            case MAP:
                int mapSize = unpacker.unpackMapHeader();
                Map<Object, Object> map = new java.util.HashMap<>();
                for (int i = 0; i < mapSize; i++) {
                    Object key = unpack(unpacker);
                    Object value = unpack(unpacker);
                    map.put(key, value);
                }
                return map;
            default:
                throw new IOException("Unknown format");
        }
    }

}
