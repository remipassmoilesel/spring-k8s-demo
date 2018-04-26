package org.remipassmoilesel.microservice_commons.sync;

import io.nats.client.*;
import org.remipassmoilesel.microservice_commons.common.Helpers;
import org.remipassmoilesel.microservice_commons.common.RemoteException;
import org.remipassmoilesel.microservice_commons.common.Serializer;

import java.io.IOException;
import java.io.Serializable;
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

        AsyncSubscription subscription = this.connection.subscribe(
                this.getCompleteSubjectFrom(subject),
                this.createHandler(subject, handler)
        );

        this.subscriptions.put(subject, subscription);
    }

    public Serializable[] publish(String subject, Serializable... args) {
        Helpers.checkSubjectString(subject);

        try {
            Message rawResponse = this.connection.request(this.getCompleteSubjectFrom(subject), Serializer.serialize(args));
            return this.handleRemoteResponse(rawResponse);
        } catch (RemoteException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    private Serializable[] handleRemoteResponse(Message rawResponse) throws RemoteException {
        Serializable[] deserializedResp = Serializer.deserialize(rawResponse.getData());

        if (deserializedResp != null && deserializedResp.length > 0) {
            Serializable maybeError = deserializedResp[0];
            if (maybeError instanceof RemoteException) {
                throw (RemoteException) maybeError;
            }
        }

        return deserializedResp;
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

    private MessageHandler createHandler(String subject, SyncHandler handler) {
        return (Message natsMessage) -> {
            System.out.println("Received a message on subject: " + subject);

            try {
                Serializable[] deserialized = Serializer.deserialize(natsMessage.getData());
                Serializable[] handlingResult = handler.handle(subject, deserialized);

                byte[] reply;
                if (handlingResult != null) {
                    reply = Serializer.serialize(handlingResult);
                } else {
                    reply = new byte[]{};
                }

                this.connection.publish(natsMessage.getReplyTo(), reply);

            } catch (Exception e) {
                this.handleRemoteException(natsMessage.getReplyTo(), e);
            }

        };
    }

    @Override
    protected void finalize() throws Throwable {
        this.connection.close();
    }
}
