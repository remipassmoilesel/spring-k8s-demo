package org.remipassmoilesel.k8sdemo.commons;

import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;

public abstract class AbstractSyncClient {

    protected final MicroCommSync microCommSync;

    public AbstractSyncClient(MicroCommSync microCommSync) {
        this.microCommSync = microCommSync;
    }

}
