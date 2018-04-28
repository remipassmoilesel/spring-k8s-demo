package org.remipassmoilesel.microservice_commons.comm.sync;

import org.junit.Test;
import org.remipassmoilesel.microservice_commons.comm.common.MCMessage;
import org.remipassmoilesel.microservice_commons.comm.common.Serializer;

import java.io.IOException;
import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SerializerTest {

    @Test
    public void serializeSimpleTypesShouldSucceed() throws IOException, ClassNotFoundException {
        byte[] result = Serializer.serialize(new Serializable[]{1, 2, 5, 6});
        Serializable[] unserialized = (Serializable[]) Serializer.deserialize(result);
        assertThat(unserialized.length, equalTo(4));
        assertThat(unserialized[2], equalTo(5));
    }

    @Test
    public void serializeMessageShouldSucceed() throws IOException, ClassNotFoundException {
        byte[] result = Serializer.serialize(MCMessage.fromObjects(1, 2, 5, 6));
        MCMessage unserialized = (MCMessage) Serializer.deserialize(result);
        assertThat(unserialized.getContent().length, equalTo(4));
        assertThat(unserialized.getAsInt(2), equalTo(5));
    }

}
