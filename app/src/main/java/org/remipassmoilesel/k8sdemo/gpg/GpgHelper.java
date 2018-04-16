package org.remipassmoilesel.k8sdemo.gpg;

import org.apache.commons.exec.ExecuteException;
import org.remipassmoilesel.k8sdemo.document.Document;
import org.remipassmoilesel.k8sdemo.document.DocumentHelper;
import org.remipassmoilesel.k8sdemo.utils.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

public class GpgHelper {

    private GpgKey defaultGpgKeys;
    private GpgWrapper gpgw = new GpgWrapper();
    private DocumentHelper docHelper = new DocumentHelper();

    public String signDocument(Document doc, GpgKey key) throws IOException {
        this.checkGpgKeys(key);
        this.docHelper.checkDocumentContent(doc);

        gpgw.importPrivateKey(key.getPrivateKey());
        gpgw.importPublicKey(key.getPublicKey());

        Path tempPath = docHelper.persistDocContentTemporary(doc);

        String signature = gpgw.signDocument(
                tempPath.toAbsolutePath().toString(),
                key.getEmail()
        );
        docHelper.deleteTempDocument(tempPath);

        return signature;
    }

    public GpgValidationResult verifyDocument(Document doc, GpgKey key) throws IOException {
        this.checkGpgKeys(key);
        this.docHelper.checkDocumentContent(doc);
        this.docHelper.checkDocumentSignature(doc);

        gpgw.importPrivateKey(key.getPrivateKey());
        gpgw.importPublicKey(key.getPublicKey());

        Path docTempPath = docHelper.persistDocContentTemporary(doc);
        Path signTempPath = docHelper.persistDocSignatureTemporary(doc);

        boolean isValid;
        try {
            gpgw.verifyDocument(
                    docTempPath.toAbsolutePath().toString(),
                    signTempPath.toAbsolutePath().toString(),
                    key.getEmail()
            );
            isValid = true;
        } catch (ExecuteException e) {
            if (e.getExitValue() == 1) {
                isValid = false;
            } else {
                throw e;
            }
        } finally {
            docHelper.deleteTempDocument(docTempPath);
            docHelper.deleteTempSignature(signTempPath);
        }

        return new GpgValidationResult(doc, isValid);
    }

    public GpgKey getDefaultGpgKeys() {
        if (this.defaultGpgKeys == null) {
            this.loadDefaultKeys();
        }
        return this.defaultGpgKeys;
    }

    public void checkGpgKeys(GpgKey key) {
        if (key.getPrivateKey() == null) {
            throw new Error("Private key is null");
        }
        if (key.getPublicKey() == null) {
            throw new Error("Public key is null");
        }
    }

    private void loadDefaultKeys() {
        try {
            String privateKey = FileUtils.loadTestFileAsString("gpg-keys/private-key.pem");
            String publicKey = FileUtils.loadTestFileAsString("gpg-keys/public-key.pem");

            this.defaultGpgKeys = new GpgKey("Joe le gros", "joe@gros.fr");
            defaultGpgKeys.setPrivateKey(privateKey);
            defaultGpgKeys.setPublicKey(publicKey);

            this.checkGpgKeys(defaultGpgKeys);
        } catch (IOException e) {
            throw new RuntimeException("Unable to find keys: ", e);
        }
    }
}
