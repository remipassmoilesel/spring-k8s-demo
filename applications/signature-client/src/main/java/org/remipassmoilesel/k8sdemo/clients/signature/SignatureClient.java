package org.remipassmoilesel.k8sdemo.clients.signature;

import io.reactivex.Single;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.GpgValidationResult;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.SignedDocument;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.commons.utils.AbstractSyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SignatureClient extends AbstractSyncClient {

    private static final Logger logger = LoggerFactory.getLogger(SignatureClient.class);

    public SignatureClient(MicroCommSync microCommSync) {
        super(microCommSync);
    }

    @SuppressWarnings("unchecked")
    public Single<List<SignedDocument>> getDocuments() {
        logger.info("Requesting {}", SignatureSubjects.GET_DOCUMENTS);
        return microCommSync.request(SignatureSubjects.GET_DOCUMENTS, MCMessage.EMPTY)
                .map((message) -> (List<SignedDocument>) message.getContent()[0]);
    }

    public Single<SignedDocument> persistAndSignDocument(SignedDocument document) {
        logger.info("Requesting {}", SignatureSubjects.PERSIST_AND_SIGN_DOCUMENT);
        return microCommSync.request(SignatureSubjects.PERSIST_AND_SIGN_DOCUMENT, MCMessage.fromObject(document))
                .map(message -> (SignedDocument) message.getContent()[0]);
    }

    // TODO: return Single<Void> or better type
    public Single<MCMessage> deleteDocument(String documentId) {
        logger.info("Requesting {} {}", SignatureSubjects.DELETE_DOCUMENT, documentId);
        return microCommSync.request(SignatureSubjects.DELETE_DOCUMENT, MCMessage.fromObject(documentId));
    }

    public Single<GpgValidationResult> checkDocument(SignedDocument document, String documentId) {
        logger.info("Requesting {}", SignatureSubjects.CHECK_DOCUMENT);
        return microCommSync.request(SignatureSubjects.CHECK_DOCUMENT, MCMessage.fromObjects(document, documentId))
                .map(message -> (GpgValidationResult) message.getContent()[0]);
    }
}
