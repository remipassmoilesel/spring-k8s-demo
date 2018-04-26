package org.remipassmoilesel.microservice_commons.common;

public class RemoteException extends RuntimeException {
    private final Exception remoteException;

    public RemoteException(Exception e){
        super();
        this.remoteException = e;
    }

    public Exception getRemoteException() {
        return remoteException;
    }
}
