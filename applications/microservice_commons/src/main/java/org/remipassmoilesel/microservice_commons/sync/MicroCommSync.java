package org.remipassmoilesel.microservice_commons.sync;

import io.nats.client.*;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.remipassmoilesel.microservice_commons.common.Helpers;
import org.remipassmoilesel.microservice_commons.common.MCMessage;
import org.remipassmoilesel.microservice_commons.common.RemoteException;
import org.remipassmoilesel.microservice_commons.common.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MicroCommSync {

    private final Scheduler scheduler = Schedulers.io();
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

    public void handle(String subject, SyncHandler handler) {
        Helpers.checkSubjectString(subject);

        AsyncSubscription subscription = this.connection.subscribe(
                this.getCompleteSubjectFrom(subject),
                this.createHandler(subject, handler)
        );

        this.subscriptions.put(subject, subscription);
    }

    public Single<MCMessage> request(String subject, MCMessage mcMessage) {
        Helpers.checkSubjectString(subject);

        return Single.fromCallable(() -> {
            Message rawResponse = this.connection.request(
                    this.getCompleteSubjectFrom(subject),
                    mcMessage.serialize()
            );
            return this.handleRemoteResponse(rawResponse);
        });

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

    private MCMessage handleRemoteResponse(Message rawResponse) throws Exception {
        byte[] data = rawResponse.getData();
        if (data == null) {
            return MCMessage.EMPTY;
        }

        Serializable deserializedResp = Serializer.deserialize(data);
        if (deserializedResp instanceof RemoteException) {
            throw (RemoteException) deserializedResp;
        }

        return (MCMessage) deserializedResp;
    }

    private MessageHandler createHandler(String subject, SyncHandler handler) {
        return (Message natsMessage) -> {
            System.out.println("Received a message on subject: " + subject);

            try {
                MCMessage deserialized = (MCMessage) Serializer.deserialize(natsMessage.getData());
                MCMessage response = handler.handle(subject, deserialized);

                byte[] reply;
                if (response != null) {
                    reply = Serializer.serialize(response);
                } else {
                    reply = new byte[]{};
                }

                this.connection.publish(natsMessage.getReplyTo(), reply);
            } catch (Exception e) {
                this.handleRemoteException(natsMessage.getReplyTo(), e);
            }
        };
    }

    private void handleRemoteException(String replyTo, Exception e) {
        RemoteException remoteException = Helpers.wrapException(e);
        try {
            this.connection.publish(replyTo, Serializer.serialize(new Serializable[]{remoteException}));
        } catch (Exception e1) {
            // TODO: Add better logging
            e1.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.connection.close();
    }
}
