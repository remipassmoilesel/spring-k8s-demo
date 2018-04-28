package org.remipassmoilesel.k8sdemo.signature.document;

import org.remipassmoilesel.k8sdemo.signature.gpg.GpgHelper;
import org.remipassmoilesel.k8sdemo.signature.gpg.GpgValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class DocumentManager {

    @Autowired
    private DocumentRepository documentRepository;

    private GpgHelper gpgHelper = new GpgHelper();

    public List<Document> getDocuments() {
        return documentRepository.findAll();
    }

    public Document persistDocument(String documentName, byte[] documentContent) throws IOException {
        Document document = new Document(documentName, documentContent, new Date());

        String sign = gpgHelper.signDocument(document, gpgHelper.getDefaultGpgKeys());
        document.setSignature(sign);

        documentRepository.save(document);
        return document;
    }

    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }

    public GpgValidationResult verifyDocument(byte[] documentContent, Long originalDocumentId) throws IOException {

        Optional<Document> validDoc = documentRepository.findById(originalDocumentId);
        if (!validDoc.isPresent()) {
            throw new NullPointerException("Document not found: " + originalDocumentId);
        }

        Document candidateDocument = new Document(
                validDoc.get().getName(),
                documentContent,
                validDoc.get().getDate()
        );
        candidateDocument.setSignature(validDoc.get().getSignature());

        GpgValidationResult validationResult = gpgHelper.verifyDocument(
                candidateDocument,
                gpgHelper.getDefaultGpgKeys()
        );

        return validationResult;
    }

}
