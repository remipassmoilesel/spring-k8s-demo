package org.remipassmoilesel.k8sdemo.commons.comm.utils;

import java.io.*;

public class Serializer {

    public static byte[] serialize(Serializable obj) throws IOException {
        byte[] bytes = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        }
        return bytes;
    }

    public static Serializable deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        Serializable obj = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            obj = (Serializable) ois.readObject();
        }
        return obj;
    }

}
