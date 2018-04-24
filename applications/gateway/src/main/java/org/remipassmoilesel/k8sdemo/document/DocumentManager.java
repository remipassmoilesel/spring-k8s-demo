package org.remipassmoilesel.k8sdemo.document;

import org.remipassmoilesel.k8sdemo.gpg.GpgHelper;
import org.remipassmoilesel.k8sdemo.gpg.GpgValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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

    @Transactional
    public GpgValidationResult verifyDocument(byte[] documentContent, Long originalDocumentId) throws IOException {

        Document validDoc = documentRepository.getOne(originalDocumentId);

        Document candidateDocument = new Document(validDoc.getName(), documentContent, validDoc.getDate());
        candidateDocument.setSignature(validDoc.getSignature());

        GpgValidationResult validationResult = gpgHelper.verifyDocument(candidateDocument, gpgHelper.getDefaultGpgKeys());

        return validationResult;
    }

}
