package org.remipassmoilesel.microservice_commons.sync;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.remipassmoilesel.microservice_commons.common.MCMessage;
import org.remipassmoilesel.microservice_commons.common.RemoteException;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MicroCommSyncTest {

    private MicroCommSync microComm;

    @Before
    public void testSetup() throws IOException {
        microComm = TestHelpers.newSync();
    }

    @Test
    public void messageShouldBeWellReceived() {
        String subject = TestHelpers.getRandomSubject("testsubject");
        MCMessage requestMessage = MCMessage.fromObject("test-message-sent");
        MCMessage replyMessage = MCMessage.fromObject("test-message-replied");

        microComm.handle(subject, (String subj, MCMessage message) -> {
            assertThat(message, equalTo(requestMessage));
            return replyMessage;
        });

        Single<MCMessage> reply = microComm.request(subject, requestMessage);
        TestObserver<MCMessage> test = reply.test();
        reply.blockingGet();

        test.assertComplete();
        test.assertValue(replyMessage);
    }

    // TODO: improve error handling with rxjava
    @Test(expected = RemoteException.class)
    public void errorsShouldBeWellReceived() {
        String subject = TestHelpers.getRandomSubject("testsubject");
        MCMessage requestMessage = MCMessage.fromObject("test-message-sent");

        microComm.handle(subject, (String subj, MCMessage message) -> {
            throw new NullPointerException("You tried to forgot me ??? I'm back !");
        });

        Single<MCMessage> reply = microComm.request(subject, requestMessage);
        reply.blockingGet();
    }

}
