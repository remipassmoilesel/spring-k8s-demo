package org.remipassmoilesel.k8sdemo.commons.comm.utils;

public class RemoteException extends RuntimeException {

    public static RemoteException wrap(Exception e) {
        return new RemoteException(e);
    }

    public RemoteException(Exception e) {
        super(e);
    }

}
