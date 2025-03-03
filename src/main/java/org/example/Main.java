package org.example;

import org.apache.avro.generic.GenericRecord;
import org.example.avro.AvroConverter;
import org.example.messagepack.MessagePackConverter;
import org.example.protobuffer.ProtoBufferConverter;
import org.example.thrift.ThriftConverter;

import java.io.File;



public class Main {
    public static void main(String[] args) {

//        Read JSON file & convert to MessagePack file
        String inputPath = "myJson.json";
//
        // Convert Map to MessagePack
        byte[] msgPackBytes = MessagePackConverter.convertToMessagePack(inputPath);

        // Save MessagePack to file
        MessagePackConverter.writeMessagePack("myMsgPack.msgpack", msgPackBytes);
//        ============================================================================================

//        Convert to avro
        String inputSchema = "menu.avsc";

        AvroConverter.readAvroSchema(inputSchema);

        GenericRecord menuRecord = AvroConverter.convertToAvro(inputPath);

        AvroConverter.writeAvro(menuRecord, "myAvro.avro");

//        ============================================================================================

//        Convert to proto

        byte[] protoBytes = ProtoBufferConverter.convertToProto(inputPath);

        ProtoBufferConverter.writeProto("myProto.bin", protoBytes);
//        ============================================================================================

//        Convert to threft

        byte[] thriftBytes = ThriftConverter.convertToThrift(inputPath);

        ThriftConverter.writeThrift("myThrift.bin", thriftBytes);

        File json = new File("myJson.json");
        File avro = new File("myAvro.avro");
        File proto = new File("myProto.bin");
        File thrift = new File("myThrift.bin");
        File msgpack = new File("myMsgPack.msgpack");


        System.out.println("json size: " + json.length() + " bytes");
        System.out.println("avro size: " + avro.length() + " bytes");
        System.out.println("proto size: " + proto.length() + " bytes");
        System.out.println("thrift size: " + thrift.length() + " bytes");
        System.out.println("msgpack size: " + msgpack.length() + " bytes");
    }
}