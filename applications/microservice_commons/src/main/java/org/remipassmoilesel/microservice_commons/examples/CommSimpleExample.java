package org.remipassmoilesel.microservice_commons.examples;

import io.reactivex.Observable;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.remipassmoilesel.microservice_commons.LoggerConfig;
import org.remipassmoilesel.microservice_commons.common.MCMessage;
import org.remipassmoilesel.microservice_commons.sync.MicroCommSync;
import org.remipassmoilesel.microservice_commons.sync.MicroCommSyncConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CommSimpleExample {

    public static void main(String[] args) throws IOException {
        LoggerConfig.configureLog(Level.INFO);

        Logger.info("Starting example ...");
        MicroCommSync comm = connect();

        String subject = "example_subject";
        comm.handle(subject, (useSubject, message) -> {
            Long arg1 = message.getAsLong(0);
            Long arg2 = message.getAsLong(1);

            Logger.info("Receiving request: {} + {}", arg1, arg2);
            return MCMessage.fromObject(arg1 + arg2);
        });

        Observable.interval(2, TimeUnit.SECONDS)
                .flatMapSingle((tick) -> comm.request(subject, MCMessage.fromObjects(2L, tick)))
                .subscribe((message) -> {
                    Logger.info("Result: {}", message.getAsLong(0));
                });
    }

    private static MicroCommSync connect() throws IOException {
        MicroCommSyncConfig config = new MicroCommSyncConfig("sampleapp", "nats://localhost:4222");
        MicroCommSync comm = new MicroCommSync(config);
        comm.connect();
        return comm;
    }

}
