package org.remipassmoilesel.microservice_commons.common;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class MCMessage implements Serializable {

    public static final MCMessage EMPTY = new MCMessage();

    public static MCMessage fromObject(Serializable content) {
        return new MCMessage(new Serializable[]{content});
    }

    public static MCMessage fromObjects(Serializable... content) {
        return new MCMessage(content);
    }

    public static MCMessage fromArray(Serializable[] content) {
        return new MCMessage(content);
    }

    public static MCMessage deserialize(byte[] serialized) throws IOException, ClassNotFoundException {
        return (MCMessage) Serializer.deserialize(serialized);
    }

    private Serializable[] content;

    public MCMessage() {
        this.content = new Serializable[]{};
    }

    public MCMessage(Serializable[] content) {
        this.setContent(content);
    }

    public Serializable[] getContent() {
        return content;
    }

    public void setContent(Serializable[] content) {
        this.content = content;
    }

    public String getAsString(int index) {
        return (String) this.content[index];
    }

    public Integer getAsInt(int index) {
        return (Integer) this.content[index];
    }

    @Override
    public String toString() {
        return "MCMessage{" +
                "content=" + Arrays.toString(content) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MCMessage mcMessage = (MCMessage) o;
        return Arrays.equals(content, mcMessage.content);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }

    public byte[] serialize() throws IOException {
        return Serializer.serialize(this);
    }
}
