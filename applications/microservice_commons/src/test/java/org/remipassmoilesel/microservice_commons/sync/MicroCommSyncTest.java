//package org.remipassmoilesel.microservice_commons.sync;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.remipassmoilesel.microservice_commons.common.Response;
//
//import java.io.IOException;
//import java.io.Serializable;
//
//import static org.junit.Assert.assertArrayEquals;
//
//public class MicroCommSyncTest {
//
//    private MicroCommSync microComm;
//
//    @Before
//    public void testSetup() throws IOException {
//        microComm = TestHelpers.newSync();
//    }
//
//    // FIXME: not functionnal, try with rxjava ?
//    @Test
//    public void messageShouldBeWellReceived() {
//        String subject = TestHelpers.getRandomSubject("testsubject");
//        Serializable[] testMessage = new Serializable[]{"test-message"};
//
//        microComm.handle(subject, (String subj, Serializable[] args) -> {
//            assertArrayEquals(args, testMessage);
//            return Response.EMPTY;
//        });
//
//        microComm.request(subject, testMessage);
//    }
//
//}
