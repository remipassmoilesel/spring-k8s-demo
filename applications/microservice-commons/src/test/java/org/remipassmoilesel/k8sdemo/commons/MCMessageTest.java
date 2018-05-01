package org.remipassmoilesel.k8sdemo.commons;

import org.junit.Test;
import org.remipassmoilesel.k8sdemo.commons.comm.MCMessage;
import org.remipassmoilesel.k8sdemo.commons.comm.utils.Serializer;

import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class MCMessageTest {

    @Test
    public void typingUtilsShouldWorkOnSimpleTypes() {
        MCMessage message = MCMessage.fromObjects(1, "a", 5l);
        assertThat(MCMessage.getContent(message, 0, Integer.class), equalTo(1));
        assertThat(MCMessage.getContent(message, 1, String.class), equalTo("a"));
        assertThat(MCMessage.getContent(message, 2, Long.class), equalTo(5l));

        assertThat(message.getAsInt(0), equalTo(1));
        assertThat(message.getAsString(1), equalTo("a"));
        assertThat(message.getAsLong(2), equalTo(5l));
    }

    @Test(expected = IllegalArgumentException.class)
    public void typingUtilsShouldThrowIfTypeIsIncorrect() {
        MCMessage message = MCMessage.fromObjects(1, "a", 5l);
        MCMessage.getContent(message, 0, String.class);
    }

    @Test
    public void typingUtilsShouldWorkOnComplexType() {
        ArrayList<MCMessage> list = new ArrayList<>();
        list.add(new MCMessage());

        MCMessage message = MCMessage.fromObject(list);
        assertThat(MCMessage.getContent(message, 0, ArrayList.class), equalTo(list));
    }

}
