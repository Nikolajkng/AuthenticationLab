package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrinterInterface extends Remote {

    // Declaring methods prototypes:
    public void print(ClientSession session, String filename, String printer) throws RemoteException;

    public void queue(ClientSession session, String printer) throws RemoteException;

    public void topQueue(ClientSession session, String printer, int job) throws RemoteException;

    public void start(ClientSession session) throws RemoteException;

    public void stop(ClientSession session) throws RemoteException;

    public void restart(ClientSession session) throws RemoteException;

    public void status(ClientSession session, String printer) throws RemoteException;

    public void setConfig(ClientSession session, String parameter, String value) throws RemoteException;

    public void readConfig(ClientSession session, String parameter) throws RemoteException;

    public void updateRoles(ClientSession session, RoleChange[] added, RoleChange[] removed) throws RemoteException;

    public ClientSession login(String username, String password) throws RemoteException;

    public ClientSession register(String username, String password) throws RemoteException;

}
