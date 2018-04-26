package org.remipassmoilesel.microservice_commons.examples;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.remipassmoilesel.microservice_commons.common.RemoteException;
import org.remipassmoilesel.microservice_commons.sync.MicroCommSync;
import org.remipassmoilesel.microservice_commons.sync.MicroCommSyncConfig;

import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleExample {

    public static void main(String[] args) throws IOException {

        System.out.println("Starting main");

        MicroCommSyncConfig config = new MicroCommSyncConfig("sampleapp", "nats://localhost:4222");
        MicroCommSync comm = new MicroCommSync(config);

        System.out.println("Connecting");
        comm.connect();
        System.out.println("Connected");

        String subject = "example_subject";

        System.out.println("Subscribing");
        comm.subscribe(subject, (useSubject, messageArgs) -> {
            System.out.println("Receiving request: " + ReflectionToStringBuilder.toString(messageArgs));
            int arg1 = (int) messageArgs[0];
            int arg2 = (int) messageArgs[1];
            return new Serializable[]{arg1 + arg2};
        });
        System.out.println("Subscribed");

        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                System.out.println("Sending message");
                try {
                    // TODO: serialize complex objects too: new ExampleMessage("example message")
                    System.out.println("Sending message");
                    Object response = comm.publish(subject, 1, 2, "test", new ExampleMessage("example message"));
                    String responseStr = response != null ? ReflectionToStringBuilder.toString(response) : null;
                    System.out.println("Message sent. Response: " + responseStr);
                    System.out.println();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer("Timer");

        System.out.println("Starting timer");
        long delay = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);

    }

    public static class ExampleMessage implements Serializable {

        private final String content;

        ExampleMessage(String content) {
            this.content = content;
        }

    }

}
