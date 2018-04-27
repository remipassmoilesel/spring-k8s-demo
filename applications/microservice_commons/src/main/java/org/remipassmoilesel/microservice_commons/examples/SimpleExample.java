package org.remipassmoilesel.microservice_commons.examples;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.remipassmoilesel.microservice_commons.common.MCMessage;
import org.remipassmoilesel.microservice_commons.sync.MicroCommSync;
import org.remipassmoilesel.microservice_commons.sync.MicroCommSyncConfig;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleExample {

    public static void main(String[] args) throws IOException {

        System.out.println("Starting example ...");
        MicroCommSync comm = connect();

        String subject = "example_subject";
        comm.handle(subject, (useSubject, message) -> {
            System.out.println("Receiving request: " + ReflectionToStringBuilder.toString(message));

            int arg1 = message.getAsInt(0);
            int arg2 = message.getAsInt(1);
            return MCMessage.fromObject(arg1 + arg2);
        });

        final int[] i = {0};
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                i[0]++;
                comm.request(subject, MCMessage.fromObjects(2, i[0]))
                        .subscribe((message) -> {
                            System.out.println("MCMessage sent. Response: " + message.getAsInt(0));
                            System.out.println();
                        });
            }
        };
        Timer timer = new Timer("Timer");

        timer.scheduleAtFixedRate(repeatedTask, 0L, 1000L);
    }

    private static MicroCommSync connect() throws IOException {
        MicroCommSyncConfig config = new MicroCommSyncConfig("sampleapp", "nats://localhost:4222");
        MicroCommSync comm = new MicroCommSync(config);
        comm.connect();
        return comm;
    }

}
