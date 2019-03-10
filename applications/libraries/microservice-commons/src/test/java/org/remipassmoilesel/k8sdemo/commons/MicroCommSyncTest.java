package org.remipassmoilesel.k8sdemo.commons;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;
import org.remipassmoilesel.k8sdemo.commons.comm.utils.RemoteException;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MicroCommSyncTest {

    private MicroCommSync microComm1;
    private MicroCommSync microComm2;

    @Before
    public void testSetup() throws IOException {
        microComm1 = TestHelpers.newSync();
        microComm1.connect();
        microComm2 = TestHelpers.newSync();
        microComm2.connect();
    }

    @Test(expected = IllegalArgumentException.class)
    public void badSubjectShouldThrow() {
        String badSubject = "test@subject";
        microComm1.handle(badSubject, (s, m) -> MCMessage.EMPTY);
    }

    @Test(expected = IllegalStateException.class)
    public void nonConnectedShouldThrow() throws IOException {
        MicroCommSync nonConnected = TestHelpers.newSync();
        nonConnected.handle("testsubject", (s, m) -> MCMessage.EMPTY);
    }

    @Test
    public void messageShouldBeWellReceived() {
        String subject = TestHelpers.getRandomSubject("testsubject");
        MCMessage requestMessage = MCMessage.fromObject("test-message-sent");
        MCMessage replyMessage = MCMessage.fromObject("test-message-replied");

        microComm1.handle(subject, (String subj, MCMessage message) -> {
            assertThat(message, equalTo(requestMessage));
            return replyMessage;
        });

        Single<MCMessage> reply = microComm2.request(subject, requestMessage);
        TestObserver<MCMessage> test = reply.test();
        reply.blockingGet();

        test.assertComplete();
        test.assertValue(replyMessage);
    }

    @Test
    public void errorsShouldBeWellReceived() {
        String subject = TestHelpers.getRandomSubject("testsubject");
        MCMessage requestMessage = MCMessage.fromObject("test-message-sent");

        microComm1.handle(subject, (String subj, MCMessage message) -> {
            throw new NullPointerException("You tried to forgot me ??? I'm back !");
        });

        Single<MCMessage> reply = microComm2.request(subject, requestMessage);
        reply.test().assertError(RemoteException.class);
    }

}
