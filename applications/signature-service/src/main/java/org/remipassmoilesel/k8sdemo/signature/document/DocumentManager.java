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

    public List<SignedDocument> getDocuments() {
        return documentRepository.findAll();
    }

    public SignedDocument getDocumentById(String id) {
        Optional<SignedDocument> optionnal = documentRepository.findById(id);
        if (!optionnal.isPresent()) {
            throw new NullPointerException("Document not found: " + id);
        }
        return optionnal.get();
    }

    public SignedDocument persistDocument(String documentName, byte[] documentContent) throws IOException {
        SignedDocument document = new SignedDocument(documentName, documentContent, new Date());

        String sign = gpgHelper.signDocument(document, gpgHelper.getDefaultGpgKeys());
        document.setSignature(sign);

        documentRepository.save(document);
        return document;
    }

    public void deleteDocument(String documentId) {
        documentRepository.deleteById(documentId);
    }

    public GpgValidationResult verifyDocument(byte[] documentContent, String originalDocumentId) throws IOException {

        Optional<SignedDocument> validDoc = documentRepository.findById(originalDocumentId);
        if (!validDoc.isPresent()) {
            throw new NullPointerException("Document not found: " + originalDocumentId);
        }

        SignedDocument candidateDocument = new SignedDocument(
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
