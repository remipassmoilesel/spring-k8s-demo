package org.remipassmoilesel.k8sdemo.commons;

import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;

public abstract class AbstractSyncServer {

    protected final MicroCommSync microCommSync;

    public AbstractSyncServer(MicroCommSync microCommSync) {
        this.microCommSync = microCommSync;
    }

    protected abstract void registerHandlers();

}
