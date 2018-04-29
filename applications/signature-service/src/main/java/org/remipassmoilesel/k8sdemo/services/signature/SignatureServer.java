package org.remipassmoilesel.k8sdemo.services.signature;

import org.remipassmoilesel.k8sdemo.clients.signature.GpgValidationResult;
import org.remipassmoilesel.k8sdemo.clients.signature.SignedDocument;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.commons.utils.AbstractSyncServer;
import org.remipassmoilesel.k8sdemo.services.signature.document.DocumentManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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

        microCommSync.handle("getDocuments", (subject, message) -> {
            logger.trace("Server received request for: getDocuments");

            List<SignedDocument> documents = documentManager.getDocuments();
            return MCMessage.fromList(documents);
        });

        microCommSync.handle("persistAndSignDocument", (subject, message) -> {
            logger.trace("Server received request for: persistAndSignDocument");

            SignedDocument document = this.getDocumentFromMessage(message, 0);
            SignedDocument persisted = documentManager.persistDocument(document.getName(), document.getContent());
            return MCMessage.fromObject(persisted);
        });

        microCommSync.handle("deleteDocument", (subject, message) -> {
            logger.trace("Server received request for: deleteDocument");

            String documentId = message.getAsString(0);
            documentManager.deleteDocument(documentId);
            return MCMessage.EMPTY;
        });

        microCommSync.handle("checkDocument", (subject, message) -> {
            logger.trace("Server received request for: checkDocument");

            SignedDocument document = this.getDocumentFromMessage(message, 0);
            String documentId = message.getAsString(1);
            GpgValidationResult validationResult = documentManager.verifyDocument(document.getContent(), documentId);
            return MCMessage.fromObject(validationResult);
        });
    }

    private SignedDocument getDocumentFromMessage(MCMessage message, int i) {
        return (SignedDocument) message.getContent()[i];
    }
}
