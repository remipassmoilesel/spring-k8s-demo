package org.remipassmoilesel.microservice_commons.comm.sync;

import org.remipassmoilesel.microservice_commons.comm.common.MCMessage;

public interface SyncHandler {
    public MCMessage handle(String subject, MCMessage message);
}
