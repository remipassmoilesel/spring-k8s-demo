package org.remipassmoilesel.k8sdemo.commons.comm.sync;

import io.nats.client.*;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.utils.Helpers;
import org.remipassmoilesel.k8sdemo.commons.comm.utils.RemoteException;
import org.remipassmoilesel.k8sdemo.commons.comm.utils.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
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

    public MicroCommSync(MicroCommSyncConfig config) {
        this.config = config;
        logger.info("Initialized with configuration: {}", config);
    }

    public void connect() throws IOException {
        logger.info("Connecting to: {}", config.getUrl());
        this.connection = Nats.connect(config.getUrl());
    }

    public AsyncSubscription handle(String subject, SyncHandler handler) {
        String completeSubject = this.getCompleteSubjectFrom(subject);
        logger.info("Registering handler on subject: {}", completeSubject);

        this.checkConnection();
        Helpers.checkSubjectString(completeSubject);

        AsyncSubscription subscription = this.connection.subscribe(
                completeSubject,
                // Subject is used as a queue group name,
                // in order to load balance messages between instances
                completeSubject,
                this.createMessageHandler(completeSubject, handler)
        );

        return subscription;
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
                    // FIXME: wait time is too great, because requests are long at application startup
                    5,
                    TimeUnit.SECONDS
            );
            return this.handleRemoteResponse(subject, Optional.ofNullable(rawResponse));
        }).observeOn(this.scheduler);

    }

    public void disconnect(){
        this.checkConnection();
        this.connection.close();
    }

    private String getCompleteSubjectFrom(String subject) {
        return this.config.getContext() + "." + subject;
    }

    private MCMessage handleRemoteResponse(String subject, Optional<Message> rawResponse) throws Exception {

        if(!rawResponse.isPresent()){
            throw new IllegalStateException("Request timed out for subject: " + subject);
        }

        byte[] data = rawResponse.get().getData();
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

    private MessageHandler createMessageHandler(String completeSubject, SyncHandler handler) {
        return (Message natsMessage) -> {
            logger.info("Handling a message on subject: {}", completeSubject);

            try {
                MCMessage deserialized = (MCMessage) Serializer.deserialize(natsMessage.getData());
                Optional<MCMessage> response = Optional.ofNullable(
                        handler.handle(completeSubject, deserialized)
                );

                byte[] reply;
                if (response.isPresent()) {
                    reply = Serializer.serialize(response.get());
                } else {
                    reply = new byte[]{};
                }

                this.connection.publish(natsMessage.getReplyTo(), reply);
            } catch (Exception e) {
                logger.error("Error while handling message:", e);
                this.sendMessageHandlerException(natsMessage.getReplyTo(), e);
            }
        };
    }

    private void sendMessageHandlerException(String replyTo, Exception e) {
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
