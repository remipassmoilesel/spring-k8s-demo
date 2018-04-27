package org.remipassmoilesel.microservice_commons.sync;

import org.remipassmoilesel.microservice_commons.common.MCMessage;

public interface SyncHandler {
    public MCMessage handle(String subject, MCMessage message);
}
