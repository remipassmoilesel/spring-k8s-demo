package org.remipassmoilesel.k8sdemo.clients.signature.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public abstract class AbstractSignedDocument implements Serializable {

    protected String id;

    protected String name;

    protected Date date;

    protected byte[] content;

    protected String signature;

    public AbstractSignedDocument() {
    }

    public AbstractSignedDocument(String name, byte[] content, Date date) {
        this.name = name;
        this.date = date;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSignedDocument that = (AbstractSignedDocument) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(date, that.date) &&
                Arrays.equals(content, that.content) &&
                Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id, name, date, signature);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    public abstract String toString();
}
