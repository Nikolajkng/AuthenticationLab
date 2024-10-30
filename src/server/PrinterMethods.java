package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterMethods extends UnicastRemoteObject implements PrinterInterface {

    Auth auth;

    protected PrinterMethods(Auth auth) throws RemoteException {
        super();
        this.auth = auth;
    }

    // Overwrite prototype methods with actual functionality:
    public void print(String filename, String printer) throws RemoteException {

        System.out.println("Invoked print");
        System.out.println("Filename of " + printer + ": " + filename);
    }

    public void queue(String printer) throws RemoteException {
        System.out.println("Invoked queue");

    }

    public void topQueue(String printer, int job) throws RemoteException {
        System.out.println("Invoked topQueue");

    }

    public void start() throws RemoteException {
        System.out.println("Invoked start");

    }

    public void stop() throws RemoteException {
        System.out.println("Invoked stop");

    }

    public void restart() throws RemoteException {
        System.out.println("Invoked restart");

    }

    public void status(String printer) throws RemoteException {
        System.out.println("Invoked status");

    }

    public void setConfig(String parameter, String value) throws RemoteException {
        System.out.println("Invoked setConfig");

    }

    public void readConfig(String parameter) throws RemoteException {
        System.out.println("Invoked readConfig");

    }

}