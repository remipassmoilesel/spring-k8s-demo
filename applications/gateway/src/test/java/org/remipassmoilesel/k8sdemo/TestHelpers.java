package org.remipassmoilesel.k8sdemo;

import org.remipassmoilesel.k8sdemo.clients.signature.SignedDocument;
import org.remipassmoilesel.k8sdemo.commons.utils.FileUtils;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class TestHelpers {

    public static SignedDocument getTestDocument(int documentId) throws IOException {
        SignedDocument doc = new SignedDocument();
        doc.setName("Test document " + documentId);
        doc.setDate(new Date());
        doc.setContent(FileUtils.loadTestFileAsByteArray("test-documents/document" + documentId + ".odt"));
        return doc;
    }

}
