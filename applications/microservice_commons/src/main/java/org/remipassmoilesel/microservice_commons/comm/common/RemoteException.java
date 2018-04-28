package org.remipassmoilesel.microservice_commons.comm.common;

public class RemoteException extends RuntimeException {

    public static RemoteException wrap(Exception e) {
        return new RemoteException(e);
    }

    public RemoteException(Exception e) {
        super(e);
    }

}
