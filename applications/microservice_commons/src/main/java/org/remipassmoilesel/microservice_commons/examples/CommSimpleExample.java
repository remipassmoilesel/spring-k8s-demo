package org.remipassmoilesel.microservice_commons.examples;

import io.reactivex.Observable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.remipassmoilesel.microservice_commons.common.MCMessage;
import org.remipassmoilesel.microservice_commons.sync.MicroCommSync;
import org.remipassmoilesel.microservice_commons.sync.MicroCommSyncConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CommSimpleExample {

    private final static Logger logger = Logger.getLogger(CommSimpleExample.class.getSimpleName());

    public static void main(String[] args) throws IOException {

        logger.info("Starting example ...");
        MicroCommSync comm = connect();

        String subject = "example_subject";
        comm.handle(subject, (useSubject, message) -> {
            int arg1 = message.getAsInt(0);
            Long arg2 = message.getAsLong(1);

            logger.info(String.format("Receiving request: %s  %s", arg1, arg2));

            return MCMessage.fromObject(arg1 + arg2);
        });

        Observable.interval(2, TimeUnit.SECONDS)
                .flatMapSingle((tick) -> comm.request(subject, MCMessage.fromObjects(2, tick)))
                .subscribe((message) -> {
                    logger.info("Result: " + message.getAsLong(0));
                });
    }

    private static MicroCommSync connect() throws IOException {
        MicroCommSyncConfig config = new MicroCommSyncConfig("sampleapp", "nats://localhost:4222");
        MicroCommSync comm = new MicroCommSync(config);
        comm.connect();
        return comm;
    }

}
