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
                .map(db -> db.toSignedDocument())
                .collect(Collectors.toList());
    }

    public SignedDocument getDocumentById(String id) {
        Optional<DbSignedDocument> optionnal = documentRepository.findById(id);
        if (!optionnal.isPresent()) {
            throw new DocumentNotFoundException(id);
        }
        return optionnal.get().toSignedDocument();
    }

    public SignedDocument persistDocument(String documentName, byte[] documentContent) throws IOException {
        DbSignedDocument document = new DbSignedDocument(documentName, documentContent, new Date());

        String sign = gpgHelper.signDocument(document, gpgHelper.getDefaultGpgKeys());
        document.setSignature(sign);

        DbSignedDocument persisted = documentRepository.save(document);
        return persisted.toSignedDocument();
    }

    public void deleteDocument(String documentId) {
        documentRepository.deleteById(documentId);
    }

    public GpgValidationResult verifyDocument(byte[] documentContent, String originalDocumentId) throws IOException {

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

}
