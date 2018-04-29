package org.remipassmoilesel.k8sdemo.commons.comm.sync;

import io.nats.client.*;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.remipassmoilesel.k8sdemo.commons.comm.utils.Helpers;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.utils.RemoteException;
import org.remipassmoilesel.k8sdemo.commons.comm.utils.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MicroCommSync {

    public static final MicroCommSync connectFromParameters(String natsUrl, String context) throws IOException {
        MicroCommSyncConfig config = new MicroCommSyncConfig(natsUrl, context);
        MicroCommSync comm = new MicroCommSync(config);
        comm.connect();
        return comm;
    }

    private static final Logger logger = LoggerFactory.getLogger(MicroCommSync.class);

    private final MicroCommSyncConfig config;
    private final Scheduler scheduler = Schedulers.computation();
    private Connection connection;
    private final Map<String, AsyncSubscription> subscriptions;

    public MicroCommSync(MicroCommSyncConfig config) {
        this.config = config;
        this.subscriptions = new HashMap<>();
        logger.trace("Initialized with configuration: {}", config);
    }

    public void connect() throws IOException {
        logger.trace("Connecting to: {}", config.getUrl());
        this.connection = Nats.connect(config.getUrl());
    }

    public void handle(String subject, SyncHandler handler) {
        logger.trace("Registering handler on subject: {}", subject);

        this.checkConnection();
        Helpers.checkSubjectString(subject);

        AsyncSubscription subscription = this.connection.subscribe(
                this.getCompleteSubjectFrom(subject),
                this.createHandler(subject, handler)
        );

        this.subscriptions.put(subject, subscription);
    }

    public Single<MCMessage> request(String subject, MCMessage mcMessage) {
        logger.trace("Sending request on subject: {}", subject);

        this.checkConnection();
        Helpers.checkSubjectString(subject);

        return Single.fromCallable(() -> {
            Message rawResponse = this.connection.request(
                    this.getCompleteSubjectFrom(subject),
                    mcMessage.serialize(),
                    1,
                    TimeUnit.SECONDS
            );
            return this.handleRemoteResponse(rawResponse);
        }).observeOn(this.scheduler);

    }

    public void unsubscribe(String subject) throws IOException {
        logger.trace("Unsubscribing from subject: {}", subject);

        this.checkConnection();
        Helpers.checkSubjectString(subject);

        AsyncSubscription sub = this.subscriptions.get(subject);
        if (sub == null) {
            throw new NullPointerException("No subscription found for subject: " + subject);
        }
        sub.unsubscribe();
    }

    public void disconnect(){
        this.checkConnection();
        this.connection.close();
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
            logger.trace("Handling a message on subject: {}", subject);

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
            logger.error("Error while handling error: {}", e);
        }
    }

    private void checkConnection() {
        if(this.connection == null || !this.connection.isConnected()){
            throw new IllegalStateException("Not connected !");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.disconnect();
    }

}
