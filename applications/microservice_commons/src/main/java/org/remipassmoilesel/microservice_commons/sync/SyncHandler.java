package org.remipassmoilesel.microservice_commons.sync;

import java.io.Serializable;

public interface SyncHandler {
    public Serializable[] handle(String subject, Serializable[] args);
}
