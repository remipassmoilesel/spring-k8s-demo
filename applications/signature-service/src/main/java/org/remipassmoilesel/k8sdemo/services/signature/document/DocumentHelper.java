package org.remipassmoilesel.k8sdemo.services.signature.document;

import org.apache.commons.io.FileUtils;
import org.remipassmoilesel.k8sdemo.clients.signature.SignedDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class DocumentHelper {

    public Path persistDocContentTemporary(SignedDocument document) throws IOException {
        this.checkDocumentContent(document);

        Path tempPath = this.getTempPath();
        FileUtils.writeByteArrayToFile(tempPath.toFile(), document.getContent());

        return tempPath;
    }

    public Path persistDocSignatureTemporary(SignedDocument document) throws IOException {
        this.checkDocumentSignature(document);

        Path tempPath = this.getTempPath();
        FileUtils.writeByteArrayToFile(tempPath.toFile(), document.getSignature().getBytes());

        return tempPath;
    }

    public void deleteTempDocument(Path tempPath) throws IOException {
        Files.delete(tempPath);
    }

    public void deleteTempSignature(Path tempPath) throws IOException {
        Files.delete(tempPath);
    }

    public void checkDocumentContent(SignedDocument document) {
        if (document.getContent() == null) {
            throw new Error("Document content is null");
        }
    }

    public void checkDocumentSignature(SignedDocument document) {
        if (document.getSignature() == null) {
            throw new Error("Document signature is null");
        }
    }

    private Path getTempPath() {
        String tempName = UUID.randomUUID().toString();
        String tempDir = System.getProperty("java.io.tmpdir");
        return Paths.get(tempDir, tempName);
    }

}
