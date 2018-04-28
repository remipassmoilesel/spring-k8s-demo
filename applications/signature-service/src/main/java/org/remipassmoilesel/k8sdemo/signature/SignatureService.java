package org.remipassmoilesel.k8sdemo.signature;

import org.apache.commons.io.IOUtils;
import org.remipassmoilesel.k8sdemo.signature.document.SignedDocument;
import org.remipassmoilesel.k8sdemo.signature.document.DocumentManager;
import org.remipassmoilesel.k8sdemo.signature.gpg.GpgValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class SignatureService {
    private static final Logger logger = LoggerFactory.getLogger(SignatureService.class);

    @Autowired
    private DocumentManager documentManager;

    public List<SignedDocument> getDocuments() throws IOException {

        List<SignedDocument> documents = documentManager.getDocuments();

        // do not send document content
        documents.stream().forEach(doc -> doc.setContent(null));
        return documents;

    }

    public SignedDocument uploadAndSignDocument(MultipartFile sentDocument) throws IOException {

        InputStream docInputStream = sentDocument.getInputStream();
        String documentName = sentDocument.getOriginalFilename();
        byte[] documentContent = IOUtils.toByteArray(docInputStream);

        SignedDocument document = documentManager.persistDocument(documentName, documentContent);

        // do not send document content
        document.setContent(null);
        return document;

    }

    public void deleteDocument(String documentId) {
        documentManager.deleteDocument(documentId);
    }

    public GpgValidationResult checkIfDocumentValid(MultipartFile sentCandidate, String documentId) throws IOException {

        InputStream docInputStream = sentCandidate.getInputStream();
        byte[] documentContent = IOUtils.toByteArray(docInputStream);

        GpgValidationResult validationResult = documentManager.verifyDocument(documentContent, documentId);

        // do not send document content
        validationResult.getDocument().setContent(null);
        return validationResult;

    }
}
