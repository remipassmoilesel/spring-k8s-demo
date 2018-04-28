package org.remipassmoilesel.k8sdemo.signature.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Document(collection = "signed_documents")
public class SignedDocument {

    @Id
    private String id;

    private String name;

    private Date date;

    private byte[] content;

    private String signature;

    public SignedDocument() {
    }

    public SignedDocument(String name, byte[] content, Date date) {
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
        SignedDocument that = (SignedDocument) o;
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
}
