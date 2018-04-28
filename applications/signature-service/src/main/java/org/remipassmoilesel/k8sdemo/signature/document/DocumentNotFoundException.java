package org.remipassmoilesel.k8sdemo.signature.document;

public class DocumentNotFoundException extends RuntimeException {

    DocumentNotFoundException(String id) {
        super("Document not found: " + id);
    }

}
