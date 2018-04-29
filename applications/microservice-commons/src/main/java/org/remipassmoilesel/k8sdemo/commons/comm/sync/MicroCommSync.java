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
        logger.info("Initialized with configuration: {}", config);
    }

    public void connect() throws IOException {
        logger.info("Connecting to: {}", config.getUrl());
        this.connection = Nats.connect(config.getUrl());
    }

    public void handle(String subject, SyncHandler handler) {
        String completeSubject = this.getCompleteSubjectFrom(subject);
        logger.info("Registering handler on subject: {}", completeSubject);

        this.checkConnection();
        Helpers.checkSubjectString(completeSubject);

        AsyncSubscription subscription = this.connection.subscribe(
                completeSubject,
                this.createHandler(completeSubject, handler)
        );

        this.subscriptions.put(completeSubject, subscription);
    }

    public Single<MCMessage> request(String subject, MCMessage mcMessage) {
        String completeSubject = this.getCompleteSubjectFrom(subject);
        logger.info("Sending request on subject: {}", completeSubject);

        this.checkConnection();
        Helpers.checkSubjectString(completeSubject);

        return Single.fromCallable(() -> {
            Message rawResponse = this.connection.request(
                    completeSubject,
                    mcMessage.serialize(),
                    1,
                    TimeUnit.SECONDS
            );
            return this.handleRemoteResponse(rawResponse);
        }).observeOn(this.scheduler);

    }

    public void unsubscribe(String subject) throws IOException {
        String completeSubject = this.getCompleteSubjectFrom(subject);
        logger.info("Unsubscribing from subject: {}", completeSubject);

        this.checkConnection();
        Helpers.checkSubjectString(completeSubject);

        AsyncSubscription sub = this.subscriptions.get(completeSubject);
        if (sub == null) {
            throw new NullPointerException("No subscription found for subject: " + completeSubject);
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
            logger.info("Remote error was thrown: {}", deserializedResp.getError().getMessage());
            throw deserializedResp.getError();
        }

        return deserializedResp;
    }

    private MessageHandler createHandler(String completeSubject, SyncHandler handler) {
        return (Message natsMessage) -> {
            logger.info("Handling a message on subject: {}", completeSubject);

            try {
                MCMessage deserialized = (MCMessage) Serializer.deserialize(natsMessage.getData());
                MCMessage response = handler.handle(completeSubject, deserialized);

                byte[] reply;
                if (response != null) {
                    reply = Serializer.serialize(response);
                } else {
                    reply = new byte[]{};
                }

                this.connection.publish(natsMessage.getReplyTo(), reply);
            } catch (Exception e) {
                logger.error("Error while handling message:", e);
                this.sendHandlerException(natsMessage.getReplyTo(), e);
            }
        };
    }

    private void sendHandlerException(String replyTo, Exception e) {
        RemoteException remoteException = RemoteException.wrap(e);
        try {
            byte[] serializedError = MCMessage.fromError(remoteException).serialize();
            this.connection.publish(replyTo, serializedError);
        } catch (Exception e1) {
            logger.error("Error while handling error:", e);
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
