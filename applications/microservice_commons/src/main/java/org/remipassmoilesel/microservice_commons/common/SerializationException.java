package org.remipassmoilesel.microservice_commons.common;

public class SerializationException extends Exception {
    SerializationException(Exception e) {
        super(e);
    }
}