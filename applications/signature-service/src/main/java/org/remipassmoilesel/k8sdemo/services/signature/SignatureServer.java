package org.remipassmoilesel.k8sdemo.services.signature;

import org.remipassmoilesel.k8sdemo.clients.signature.SignatureSubjects;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.GpgValidationResult;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.SignedDocument;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.commons.utils.AbstractSyncServer;
import org.remipassmoilesel.k8sdemo.services.signature.document.DocumentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.Serializable;
import java.util.List;

@Controller
public class SignatureServer extends AbstractSyncServer {

    private static final Logger logger = LoggerFactory.getLogger(SignatureServer.class);

    @Autowired
    private DocumentManager documentManager;

    public SignatureServer(@Autowired MicroCommSync microCommSync) {
        super(microCommSync);
    }

    public void registerHandlers() {

        microCommSync.handle(SignatureSubjects.GET_DOCUMENTS, (subject, message) -> {
            logger.info("Receiving request on subject: {}", SignatureSubjects.GET_DOCUMENTS);

            List<SignedDocument> documents = documentManager.getDocuments();
            return MCMessage.fromObject((Serializable) documents);
        });

        microCommSync.handle(SignatureSubjects.PERSIST_AND_SIGN_DOCUMENT, (subject, message) -> {
            logger.info("Receiving request on subject: {}", SignatureSubjects.PERSIST_AND_SIGN_DOCUMENT);

            SignedDocument document = this.getDocumentFromMessage(message, 0);
            SignedDocument persisted = documentManager.persistDocument(document.getName(), document.getContent());
            return MCMessage.fromObject(persisted);
        });

        microCommSync.handle(SignatureSubjects.DELETE_DOCUMENT, (subject, message) -> {
            logger.info("Receiving request on subject: {}", SignatureSubjects.DELETE_DOCUMENT);

            String documentId = message.getAsString(0);

            documentManager.deleteDocument(documentId);
            return MCMessage.EMPTY;
        });


        microCommSync.handle(SignatureSubjects.CHECK_DOCUMENT, (subject, message) -> {
            logger.info("Receiving request on subject: {}", SignatureSubjects.CHECK_DOCUMENT);

            SignedDocument document = this.getDocumentFromMessage(message, 0);
            String documentId = message.getAsString(1);

            GpgValidationResult validationResult = documentManager.verifyDocument(document.getContent(), documentId);
            return MCMessage.fromObject(validationResult);
        });
    }

    private SignedDocument getDocumentFromMessage(MCMessage message, int i) {
        return MCMessage.getContentAs(message, i, SignedDocument.class);
    }
}
