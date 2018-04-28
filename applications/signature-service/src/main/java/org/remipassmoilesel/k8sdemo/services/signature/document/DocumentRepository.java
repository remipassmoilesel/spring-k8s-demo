package org.remipassmoilesel.k8sdemo.services.signature.document;

import org.remipassmoilesel.k8sdemo.clients.signature.SignedDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<SignedDocument, String> {

}

