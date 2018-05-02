package org.remipassmoilesel.k8sdemo.services.signature.document;

import org.remipassmoilesel.k8sdemo.clients.signature.entities.GpgValidationResult;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.SignedDocument;
import org.remipassmoilesel.k8sdemo.services.signature.gpg.GpgHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DocumentManager {

    @Autowired
    private DocumentRepository documentRepository;

    private GpgHelper gpgHelper = new GpgHelper();

    public List<SignedDocument> getDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(DbSignedDocument::toSignedDocument)
                .collect(Collectors.toList());
    }

    public SignedDocument getDocumentById(String documentId) {
        this.checkDocumentId(documentId);

        Optional<DbSignedDocument> optionnal = documentRepository.findById(documentId);
        if (!optionnal.isPresent()) {
            throw new DocumentNotFoundException(documentId);
        }
        return optionnal.get().toSignedDocument();
    }

    public SignedDocument persistDocument(SignedDocument documentToPersist) throws IOException {
        DbSignedDocument dbDocument = new DbSignedDocument(documentToPersist.getName(), documentToPersist.getContent(), new Date());

        String sign = gpgHelper.signDocument(dbDocument.toSignedDocument(), gpgHelper.getDefaultGpgKeys());
        dbDocument.setSignature(sign);

        DbSignedDocument persisted = documentRepository.save(dbDocument);
        return persisted.toSignedDocument();
    }

    public void deleteDocument(String documentId) {
        this.checkDocumentId(documentId);

        documentRepository.deleteById(documentId);
    }

    public GpgValidationResult verifyDocument(byte[] documentContent, String originalDocumentId) throws IOException {
        this.checkDocumentId(originalDocumentId);

        SignedDocument validDoc = this.getDocumentById(originalDocumentId);
        SignedDocument candidateDocument = new SignedDocument(
                validDoc.getName(),
                documentContent,
                validDoc.getDate()
        );
        candidateDocument.setSignature(validDoc.getSignature());

        GpgValidationResult validationResult = gpgHelper.verifyDocument(
                candidateDocument,
                gpgHelper.getDefaultGpgKeys()
        );

        return validationResult;
    }

    private void checkDocumentId(String documentId) {
        if(documentId == null || documentId.equals("null")){
            throw new NullPointerException("Document id cannot be null");
        }
    }
}
