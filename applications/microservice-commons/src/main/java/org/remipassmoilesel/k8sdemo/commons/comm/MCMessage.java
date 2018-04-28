package org.remipassmoilesel.k8sdemo.commons.comm;

import org.remipassmoilesel.k8sdemo.commons.comm.utils.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class MCMessage implements Serializable {

    public static final MCMessage EMPTY = new MCMessage();

    public static MCMessage fromError(Exception error) {
        return new MCMessage(error);
    }

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
    private Exception error;

    public MCMessage() {
        this.content = new Serializable[]{};
    }

    public MCMessage(Serializable[] content) {
        this.setContent(content);
    }

    public byte[] serialize() throws IOException {
        return Serializer.serialize(this);
    }

    public MCMessage(Exception error) {
        this.setError(error);
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
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

    public Long getAsLong(int index) {
        return (Long) this.content[index];
    }

    @Override
    public String toString() {
        return "MCMessage{" +
                "content=" + Arrays.toString(content) +
                ", error=" + error +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MCMessage mcMessage = (MCMessage) o;
        return Arrays.equals(content, mcMessage.content) &&
                Objects.equals(error, mcMessage.error);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(error);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
