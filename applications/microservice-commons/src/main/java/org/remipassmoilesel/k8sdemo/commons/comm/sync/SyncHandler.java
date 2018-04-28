package org.remipassmoilesel.k8sdemo.commons.comm.sync;

import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;

public interface SyncHandler {
    public MCMessage handle(String subject, MCMessage message);
}
