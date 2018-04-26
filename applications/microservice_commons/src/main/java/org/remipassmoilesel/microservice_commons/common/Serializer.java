package org.remipassmoilesel.microservice_commons.common;

import java.io.*;

public class Serializer {

    public static byte[] serialize(Serializable[] obj) {
        byte[] bytes = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    public static Serializable[] deserialize(byte[] bytes) {
        Serializable[] obj = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            obj = (Serializable[]) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

}
