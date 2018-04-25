package org.remipassmoilesel.microservice_commons.sync;

public interface SyncHandler {
    public Object handle(String subject, Object[] args);
}
