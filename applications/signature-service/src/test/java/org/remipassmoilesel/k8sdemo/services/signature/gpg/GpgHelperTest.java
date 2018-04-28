package org.remipassmoilesel.k8sdemo.services.signature.gpg;

import org.junit.Test;
import org.remipassmoilesel.k8sdemo.clients.signature.GpgValidationResult;
import org.remipassmoilesel.k8sdemo.clients.signature.SignedDocument;
import org.remipassmoilesel.k8sdemo.services.signature.test_helpers.TestHelpers;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class GpgHelperTest {

    public GpgHelper gpgHelper = new GpgHelper();

    @Test
    public void signDocumentShouldSucceed() throws IOException {
        GpgKey key = TestHelpers.getTestGpgKey();
        SignedDocument doc = TestHelpers.getTestDocument(1);

        String sign = gpgHelper.signDocument(doc, key);
        assertThat(sign, containsString("-BEGIN PGP SIGNATURE-"));
    }

    @Test
    public void verifyDocumentShouldSucceedIfDocumentIsValid() throws Exception {
        GpgKey key = TestHelpers.getTestGpgKey();
        SignedDocument doc1 = TestHelpers.getTestDocument(1);

        String sign = gpgHelper.signDocument(doc1, key);
        doc1.setSignature(sign);

        GpgValidationResult res = gpgHelper.verifyDocument(doc1, key);
        assertTrue(res.isValid());
    }

    @Test()
    public void verifyDocumentShouldThrowIfDocumentIsInvalid() throws Exception {
        GpgKey key = TestHelpers.getTestGpgKey();
        SignedDocument doc1 = TestHelpers.getTestDocument(1);
        SignedDocument doc2 = TestHelpers.getTestDocument(2);

        String sign = gpgHelper.signDocument(doc1, key);
        doc2.setSignature(sign);

        GpgValidationResult res = gpgHelper.verifyDocument(doc2, key);
        assertFalse(res.isValid());
    }

}
