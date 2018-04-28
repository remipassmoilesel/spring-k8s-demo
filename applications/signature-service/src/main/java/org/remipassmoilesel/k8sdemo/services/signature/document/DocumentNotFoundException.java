package org.remipassmoilesel.k8sdemo.services.signature.document;

public class DocumentNotFoundException extends RuntimeException {

    DocumentNotFoundException(String id) {
        super("Document not found: " + id);
    }

}
