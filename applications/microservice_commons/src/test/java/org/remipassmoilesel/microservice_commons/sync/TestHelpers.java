package org.remipassmoilesel.microservice_commons.sync;

import java.io.IOException;
import java.util.UUID;

public class TestHelpers {

    public static MicroCommSync newSync() throws IOException {
        MicroCommSyncConfig config = new MicroCommSyncConfig("testcontext", "nats://localhost:4222");
        MicroCommSync comm = new MicroCommSync(config);
        comm.connect();
        return comm;
    }

    public static String getRandomSubject(String prefix) {
        return prefix + "." + UUID.randomUUID().toString();
    }
}
