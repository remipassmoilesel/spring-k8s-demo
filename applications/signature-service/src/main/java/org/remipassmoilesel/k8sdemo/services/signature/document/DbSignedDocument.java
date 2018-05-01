package org.remipassmoilesel.k8sdemo.services.signature.document;


import org.remipassmoilesel.k8sdemo.clients.signature.entities.AbstractSignedDocument;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.SignedDocument;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Date;

@Document(collection = "signed_documents")
public class DbSignedDocument extends AbstractSignedDocument {

    public static DbSignedDocument fromSignedDocument(SignedDocument doc) {
        DbSignedDocument db = new DbSignedDocument();
        db.setId(doc.getId());
        db.setName(doc.getName());
        db.setDate(doc.getDate());
        db.setContent(doc.getContent());
        db.setSignature(doc.getSignature());
        return db;
    }

    @Id
    protected String id;

    public DbSignedDocument(){
        super();
    }

    public DbSignedDocument(String name, byte[] content, Date date) {
        super(name, content, date);
    }

    public SignedDocument toSignedDocument() {
        SignedDocument doc = new SignedDocument();
        doc.setId(this.getId());
        doc.setName(this.getName());
        doc.setDate(this.getDate());
        doc.setContent(this.getContent());
        doc.setSignature(this.getSignature());
        return doc;
    }

    @Override
    public String toString() {
        return "DbSignedDocument{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", content=" + Arrays.toString(content) +
                ", signature='" + signature + '\'' +
                '}';
    }
}
