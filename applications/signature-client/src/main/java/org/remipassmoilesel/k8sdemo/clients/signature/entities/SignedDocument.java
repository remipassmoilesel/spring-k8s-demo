package org.remipassmoilesel.k8sdemo.clients.signature.entities;

import java.util.Date;

public class SignedDocument extends AbstractSignedDocument {

    public SignedDocument() {
        super();
    }

    public SignedDocument(String name, byte[] content, Date date) {
        super(name, content, date);
    }
}
