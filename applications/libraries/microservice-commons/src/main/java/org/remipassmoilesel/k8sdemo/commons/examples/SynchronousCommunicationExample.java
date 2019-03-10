package org.remipassmoilesel.k8sdemo.commons.examples;

import io.reactivex.Observable;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSyncConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Here there is a very simple example of synchronous communication between
 * two remote entities.
 */
public class SynchronousCommunicationExample {

    public static void main(String[] args) throws IOException {
        info("Creating a communicator instance");
        String natsBrokerUrl = "nats://localhost:4222";

        // Context can be used to separate applications
        String context = "sampleapp";
        MicroCommSync comm1 = connect(natsBrokerUrl, context);
        MicroCommSync comm2 = connect(natsBrokerUrl, context);

        info("Setting up a handler which take two numbers as parameters, add them and return the result");
        String subject = "operations.add";
        setupRequestHandler(comm1, subject);

        info("Then start communicating with him periodically");
        Observable.interval(2, TimeUnit.SECONDS)
                .flatMapSingle((tick) -> comm2.request(subject, MCMessage.fromObjects(2L, tick)))
                .subscribe((message) -> {
                    info(String.format("Result: %s", message.getAsLong(0)));
                });
    }

    private static MicroCommSync connect(String natsBrokerUrl, String context) throws IOException {
        MicroCommSyncConfig config = new MicroCommSyncConfig(natsBrokerUrl, context);
        MicroCommSync comm = new MicroCommSync(config);
        comm.connect();
        return comm;
    }

    private static void setupRequestHandler(MicroCommSync comm, String subject) {
        comm.handle(subject, (useSubject, message) -> {
            Long arg1 = message.getAsLong(0);
            Long arg2 = message.getAsLong(1);

            info(String.format("Receiving request: %s + %s", arg1, arg2));
            return MCMessage.fromObject(arg1 + arg2);
        });
    }

    private static void info(String message){
        System.out.println(message);
    }
}
