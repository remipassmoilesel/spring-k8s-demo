package org.remipassmoilesel.k8sdemo.clients.signature.entities;

import java.util.Arrays;
import java.util.Date;

public class SignedDocument extends AbstractSignedDocument {

    public SignedDocument() {
        super();
    }

    public SignedDocument(String name, byte[] content, Date date) {
        super(name, content, date);
    }

    public SignedDocument deleteBinaryfields(){
        this.setContent(null);
        return this;
    }

    @Override
    public String toString() {
        return "SignedDocument{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", content=" + Arrays.toString(content) +
                ", signature='" + signature + '\'' +
                '}';
    }
}
