package org.remipassmoilesel.k8sdemo.signature;

import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.signature.document.DocumentManager;
import org.remipassmoilesel.k8sdemo.signature.document.SignedDocument;
import org.remipassmoilesel.k8sdemo.signature.gpg.GpgValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

// TODO: implement AbstractSyncServer

@Controller
public class SignatureServer implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SignatureServer.class);

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private MicroCommSync microCommSync;

    @Override
    public void afterPropertiesSet() throws IOException {
        microCommSync.connect();
        this.registerHandlers();
    }

    private void registerHandlers() {

        microCommSync.handle("getDocuments", (subject, message) -> {
            List<SignedDocument> documents = documentManager.getDocuments();
            return MCMessage.fromList(documents);
        });

        microCommSync.handle("persistAndSignDocument", (subject, message) -> {
            SignedDocument document = this.getDocumentFromMessage(message, 0);
            SignedDocument persisted = documentManager.persistDocument(document.getName(), document.getContent());
            return MCMessage.fromObject(persisted);
        });

        microCommSync.handle("deleteDocument", (subject, message) -> {
            String documentId = message.getAsString(0);
            documentManager.deleteDocument(documentId);
            return MCMessage.EMPTY;
        });

        microCommSync.handle("checkDocument", (subject, message) -> {
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