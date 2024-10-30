package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterInterface extends Remote {

    // Declaring methods prototypes:
    public default void print(String filename, String printer) throws RemoteException {

    }

    public default void queue(String printer) throws RemoteException {

    }

    public default void topQueue(String printer, int job) throws RemoteException {

    }

    public default void start() throws RemoteException {

    }

    public default void stop() throws RemoteException {

    }

    public default void restart() throws RemoteException {

    }

    public default void status(String printer) throws RemoteException {

    }

    public default void setConfig(String parameter, String value) throws RemoteException {

    }

    public default void readConfig(String parameter) throws RemoteException {

    }

}
