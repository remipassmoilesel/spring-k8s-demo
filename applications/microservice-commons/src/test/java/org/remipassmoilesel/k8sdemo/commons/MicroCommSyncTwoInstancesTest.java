package org.remipassmoilesel.k8sdemo.commons;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.TestObserver;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.Before;
import org.junit.Test;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.sync.MicroCommSync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class MicroCommSyncTwoInstancesTest {

    private MicroCommSync microComm1;
    private MicroCommSync microComm2;
    private MicroCommSync microComm3;

    @Before
    public void testSetup() throws IOException {
        microComm1 = TestHelpers.newSync();
        microComm1.connect();
        microComm2 = TestHelpers.newSync();
        microComm2.connect();
        microComm3 = TestHelpers.newSync();
        microComm3.connect();
    }

    @Test
    public void withTwoInstancesOnSameSubjectMessagesShouldBeLoadBalanced() {
        String subject = TestHelpers.getRandomSubject("testsubject");
        String from1 = "test-message-from-1";
        String from2 = "test-message-from-2";
        MCMessage replyMessageFrom1 = MCMessage.fromObject(from1);
        MCMessage replyMessageFrom2 = MCMessage.fromObject(from2);

        microComm1.handle(subject, (String subj, MCMessage message) -> replyMessageFrom1);
        microComm2.handle(subject, (String subj, MCMessage message) -> replyMessageFrom2);

        int messageNumber = 15;
        Observable<MCMessage> requests = Observable.range(0, messageNumber)
                .flatMap(number -> {
                    return microComm3.request(subject,
                            MCMessage.fromObject("request number " + number)).toObservable();
                });

        List<MCMessage> replies = IteratorUtils.toList(requests.blockingIterable().iterator());
        assertThat(replies, hasSize(messageNumber));

        List<MCMessage> messagesFrom1 = replies.stream().filter(m -> m.getAsString(0).equals(from1))
                .collect(Collectors.toList());
        List<MCMessage> messagesFrom2 = replies.stream().filter(m -> m.getAsString(0).equals(from2))
                .collect(Collectors.toList());

        assertThat(messagesFrom1.size(), greaterThan(3));
        assertThat(messagesFrom2.size(), greaterThan(3));

    }

}
