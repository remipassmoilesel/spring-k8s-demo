package org.remipassmoilesel.k8sdemo.commons;

import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSyncConfig;

import java.io.IOException;
import java.util.UUID;

public class TestHelpers {

    public static MicroCommSync newSync() throws IOException {
        MicroCommSyncConfig config = new MicroCommSyncConfig("nats://localhost:4222", "testcontext");
        MicroCommSync comm = new MicroCommSync(config);
        comm.connect();
        return comm;
    }

    public static String getRandomSubject(String prefix) {
        return prefix + "." + UUID.randomUUID().toString();
    }
}
