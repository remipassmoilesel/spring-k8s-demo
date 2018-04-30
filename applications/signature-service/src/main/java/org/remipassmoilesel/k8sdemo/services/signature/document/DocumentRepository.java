package org.remipassmoilesel.k8sdemo.services.signature.document;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<DbSignedDocument, String> {

}

