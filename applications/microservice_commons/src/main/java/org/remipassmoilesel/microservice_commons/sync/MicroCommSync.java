package org.remipassmoilesel.microservice_commons.sync;

import io.nats.client.AsyncSubscription;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import org.remipassmoilesel.microservice_commons.common.Helpers;
import org.remipassmoilesel.microservice_commons.common.Serializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MicroCommSync {

    private final MicroCommSyncConfig config;
    private Connection connection;
    private Map<String, AsyncSubscription> subscriptions;

    public MicroCommSync(MicroCommSyncConfig config) {
        this.config = config;
        this.subscriptions = new HashMap<>();
    }

    public void connect() throws IOException {
        this.connection = Nats.connect(config.getUrl());
    }

    public void subscribe(String subject, SyncHandler handler) {
        Helpers.checkSubjectString(subject);

        AsyncSubscription subscription = this.connection.subscribe(this.getCompleteSubjectFrom(subject), natsMessage -> {
            System.out.println("Received a message on subject: " + subject);

            try {
                Object[] deserialized = (Object[]) Serializer.deserialize(natsMessage.getData());
                Object response = handler.handle(subject, deserialized);
                if (response != null) {
                    this.connection.publish(natsMessage.getReplyTo(), Serializer.serialize(response));
                    System.out.println("Response sent !");
                } else {
                    // response message is mandatory, even if response is empty
                    this.connection.publish(natsMessage.getReplyTo(), new byte[]{});
                    System.out.println("No response to send");
                }
            } catch (Exception e) {
                // TODO: better exception handling
                e.printStackTrace();
            }

        });

        this.subscriptions.put(subject, subscription);
    }

    public Object publish(String subject, Object... args) throws IOException {
        Helpers.checkSubjectString(subject);

        try {
            Message rawResponse = this.connection.request(this.getCompleteSubjectFrom(subject), Serializer.serialize(args));
            System.out.println("rawResponse");
            if(rawResponse.getData() != null){
                return Serializer.deserialize(rawResponse.getData());
            }
            return null;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void unsubscribe(String subject) throws IOException {
        Helpers.checkSubjectString(subject);

        AsyncSubscription sub = this.subscriptions.get(subject);
        if (sub == null) {
            throw new NullPointerException("No subscription found for subject: " + subject);
        }
        sub.unsubscribe();
    }

    private String getCompleteSubjectFrom(String subject) {
        return this.config.getContext() + "." + subject;
    }

    @Override
    protected void finalize() throws Throwable {
        this.connection.close();
    }
}
