package org.remipassmoilesel.k8sdemo.commons.comm;

import org.remipassmoilesel.k8sdemo.commons.comm.utils.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MCMessage implements Serializable {

    public static <T> T getContentAs(MCMessage message, int index, Class<? extends T> clazz) {
        checkContentIndex(message, index);
        Serializable rawElement = message.content[index];
        if (!clazz.isInstance(rawElement)) {
            throw new IllegalArgumentException(String.format("Element at index %s not instance of %s", index, clazz.getName()));
        }
        return clazz.cast(rawElement);
    }

    private static void checkContentIndex(MCMessage message, int index) {
        if (index >= message.content.length) {
            throw new Error(String.format(
                    "Content at index %s not found. Message contains %s element",
                    index, message.content.length)
            );
        }
    }

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

    public static MCMessage fromList(List<? extends Serializable> content) {
        return new MCMessage(content.toArray(new Serializable[]{}));
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

    /**
     * Prefer use of static method, with better typings
     *
     * @return
     */
    @Deprecated
    public Serializable[] getContent() {
        return content;
    }

    public void setContent(Serializable[] content) {
        this.content = content;
    }

    public String getAsString(int index) {
        return MCMessage.getContentAs(this, index, String.class);
    }

    public Integer getAsInt(int index) {
        return MCMessage.getContentAs(this, index, Integer.class);
    }

    public Long getAsLong(int index) {
        return MCMessage.getContentAs(this, index, Long.class);
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
