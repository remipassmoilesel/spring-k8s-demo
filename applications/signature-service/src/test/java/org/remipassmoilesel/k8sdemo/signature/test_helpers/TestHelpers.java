package org.remipassmoilesel.k8sdemo.signature.test_helpers;

import org.remipassmoilesel.k8sdemo.signature.document.SignedDocument;
import org.remipassmoilesel.k8sdemo.signature.gpg.GpgKey;
import org.remipassmoilesel.k8sdemo.signature.utils.FileUtils;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class TestHelpers {

    public static GpgKey getTestGpgKey() throws IOException {
        GpgKey key = new GpgKey("Joe le gros", "joe@gros.fr");
        key.setPrivateKey(FileUtils.loadTestFileAsString("test-keys/private-key.pem"));
        key.setPublicKey(FileUtils.loadTestFileAsString("test-keys/public-key.pem"));

        assertNotNull(key.getPrivateKey());
        assertNotNull(key.getPublicKey());
        return key;
    }

    public static SignedDocument getTestDocument(int documentId) throws IOException {
        SignedDocument doc = new SignedDocument();
        doc.setName("Test document " + documentId);
        doc.setDate(new Date());
        doc.setContent(FileUtils.loadTestFileAsByteArray("test-documents/document" + documentId + ".odt"));
        return doc;
    }

}
