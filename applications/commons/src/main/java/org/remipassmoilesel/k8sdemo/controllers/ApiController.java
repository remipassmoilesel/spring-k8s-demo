package org.remipassmoilesel.k8sdemo.controllers;

import org.apache.commons.io.IOUtils;
import org.remipassmoilesel.k8sdemo.document.Document;
import org.remipassmoilesel.k8sdemo.document.DocumentManager;
import org.remipassmoilesel.k8sdemo.gpg.GpgValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private DocumentManager documentManager;

    public List<Document> getDocuments() throws IOException {

        List<Document> documents = documentManager.getDocuments();

        // do not send document content
        documents.stream().forEach(doc -> doc.setContent(null));
        return documents;

    }

    public Document uploadAndSignDocument(MultipartFile sentDocument) throws IOException {

        InputStream docInputStream = sentDocument.getInputStream();
        String documentName = sentDocument.getOriginalFilename();
        byte[] documentContent = IOUtils.toByteArray(docInputStream);

        Document document = documentManager.persistDocument(documentName, documentContent);

        // do not send document content
        document.setContent(null);
        return document;

    }

    public void deleteDocument(Long documentId) {
        documentManager.deleteDocument(documentId);
    }

    public GpgValidationResult checkIfDocumentValid(MultipartFile sentCandidate, Long documentId) throws IOException {

        InputStream docInputStream = sentCandidate.getInputStream();
        byte[] documentContent = IOUtils.toByteArray(docInputStream);

        GpgValidationResult validationResult = documentManager.verifyDocument(documentContent, documentId);

        // do not send document content
        validationResult.getDocument().setContent(null);
        return validationResult;

    }
}
