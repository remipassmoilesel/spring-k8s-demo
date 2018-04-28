package org.remipassmoilesel.k8sdemo.signature.document;

import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Document {

    @Id
    private Long id;

    private String name;

    private Date date;

    private byte[] content;

    private String signature;

    public Document() {
    }

    public Document(String name, byte[] content, Date date) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", content=" + Arrays.toString(content) +
                ", signature=" + signature +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id) &&
                Objects.equals(name, document.name) &&
                Objects.equals(date, document.date) &&
                Arrays.equals(content, document.content) &&
                Objects.equals(signature, document.signature);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, date, signature);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }
}
