package org.remipassmoilesel.k8sdemo.clients.signature;

import io.reactivex.Single;
import org.remipassmoilesel.k8sdemo.commons.AbstractSyncClient;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SignatureClient extends AbstractSyncClient {

    public SignatureClient(MicroCommSync microCommSync) {
        super(microCommSync);
    }

    public Single<List<SignedDocument>> getDocuments() {
        return microCommSync.request("getDocuments", MCMessage.EMPTY)
                .map((message) -> {
                    return (List<SignedDocument>) message.getContent()[0];
                });
    }

    public Single<SignedDocument> persistAndSignDocument(SignedDocument document) {
        return microCommSync.request("persistAndSignDocument", MCMessage.fromObject(document))
                .map(message -> (SignedDocument) message.getContent()[0]);
    }

    // TODO: return Single<Void> or better type
    public Single<MCMessage> deleteDocument(String documentId) {
        return microCommSync.request("deleteDocument", MCMessage.fromObject(documentId));
    }

    public Single<GpgValidationResult> checkDocument(SignedDocument document) {
        return microCommSync.request("checkDocument", MCMessage.fromObject(document))
                .map(message -> (GpgValidationResult) message.getContent()[0]);
    }
}
