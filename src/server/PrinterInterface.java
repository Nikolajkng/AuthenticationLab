package server;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterInterface extends Remote {

    // Declaring methods prototypes:
    public void print(String filename, String printer) throws RemoteException;

    public void queue(String printer) throws RemoteException;

    public void topQueue(String printer, int job) throws RemoteException;

    public void start() throws RemoteException;

    public void stop() throws RemoteException;

    public void restart() throws RemoteException;

    public void status(String printer) throws RemoteException;

    public void setConfig(String parameter, String value) throws RemoteException;

    public void readConfig(String parameter) throws RemoteException;

    public Session login(String username, String password) throws RemoteException;

}
