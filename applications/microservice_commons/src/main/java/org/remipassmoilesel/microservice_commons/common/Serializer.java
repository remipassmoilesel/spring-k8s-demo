package org.remipassmoilesel.microservice_commons.common;

import java.io.*;

public class Serializer {

    public static byte[] serialize(Object obj) throws SerializationException {
        byte[] bytes = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return bytes;
    }

    public static Object deserialize(byte[] bytes) throws SerializationException {
        Object obj = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            obj = ois.readObject();
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return obj;
    }

}
