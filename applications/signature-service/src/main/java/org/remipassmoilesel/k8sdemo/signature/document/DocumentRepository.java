package org.remipassmoilesel.k8sdemo.signature.document;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface DocumentRepository extends MongoRepository<Document, Long> {

}

