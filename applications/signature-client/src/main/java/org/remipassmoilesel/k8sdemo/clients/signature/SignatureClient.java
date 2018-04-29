package org.remipassmoilesel.k8sdemo.clients.signature;

import io.reactivex.Single;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.GpgValidationResult;
import org.remipassmoilesel.k8sdemo.clients.signature.entities.SignedDocument;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.commons.utils.AbstractSyncClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SignatureClient extends AbstractSyncClient {

    public SignatureClient(MicroCommSync microCommSync) {
        super(microCommSync);
    }

    @SuppressWarnings("unchecked")
    public Single<List<SignedDocument>> getDocuments() {
        return microCommSync.request(SignatureSubjects.GET_DOCUMENTS, MCMessage.EMPTY)
                .map((message) -> (List<SignedDocument>) message.getContent()[0]);
    }

    public Single<SignedDocument> persistAndSignDocument(SignedDocument document) {
        return microCommSync.request(SignatureSubjects.PERSIST_AND_SIGN_DOCUMENT, MCMessage.fromObject(document))
                .map(message -> (SignedDocument) message.getContent()[0]);
    }

    // TODO: return Single<Void> or better type
    public Single<MCMessage> deleteDocument(String documentId) {
        return microCommSync.request(SignatureSubjects.DELETE_DOCUMENT, MCMessage.fromObject(documentId));
    }

    public Single<GpgValidationResult> checkDocument(SignedDocument document, String documentId) {
        return microCommSync.request(SignatureSubjects.CHECK_DOCUMENT, MCMessage.fromObjects(document, documentId))
                .map(message -> (GpgValidationResult) message.getContent()[0]);
    }
}
