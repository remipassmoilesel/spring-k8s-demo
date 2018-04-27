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
import java.util.HashMap;
import java.util.Map;

public class MicroCommSync {

    private final MicroCommSyncConfig config;
    private final Scheduler scheduler = Schedulers.computation();
    private Connection connection;
    private final Map<String, AsyncSubscription> subscriptions;

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
        }).observeOn(this.scheduler);

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

        MCMessage deserializedResp = (MCMessage) Serializer.deserialize(data);
        if (deserializedResp.getError() != null) {
            throw deserializedResp.getError();
        }

        return deserializedResp;
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
                this.sendHandlerException(natsMessage.getReplyTo(), e);
            }
        };
    }

    private void sendHandlerException(String replyTo, Exception e) {
        RemoteException remoteException = RemoteException.wrap(e);

        try {
            this.connection.publish(replyTo, MCMessage.fromError(remoteException).serialize());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.connection.close();
    }
}
