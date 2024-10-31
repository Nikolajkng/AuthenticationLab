package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterInterface extends Remote {

    // Declaring methods prototypes:
    public void print(Session session, String filename, String printer) throws RemoteException;

    public void queue(Session session, String printer) throws RemoteException;

    public void topQueue(Session session, String printer, int job) throws RemoteException;

    public void start(Session session) throws RemoteException;

    public void stop(Session session) throws RemoteException;

    public void restart(Session session) throws RemoteException;

    public void status(Session session, String printer) throws RemoteException;

    public void setConfig(Session session, String parameter, String value) throws RemoteException;

    public void readConfig(Session session, String parameter) throws RemoteException;

    public Session login(String username, String password) throws RemoteException;

    public Session register(String username, String password) throws RemoteException;

}
