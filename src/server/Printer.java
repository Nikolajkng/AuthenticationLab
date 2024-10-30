package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Printer extends UnicastRemoteObject implements PrinterInterface {

    Auth auth;

    protected Printer(Auth auth) throws RemoteException {
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
        System.out.println("Printer: " + printer);

    }

    public void topQueue(String printer, int job) throws RemoteException {
        System.out.println("Invoked topQueue");
        System.out.println("Printer: " + printer);
        System.out.println("Job: " + job);


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
        System.out.println("Printer: " + printer);
    }

    public void setConfig(String parameter, String value) throws RemoteException {
        System.out.println("Invoked setConfig");
        System.out.println("Parameter: " + parameter);
        System.out.println("Value: " + value);


    }

    public void readConfig(String parameter) throws RemoteException {
        System.out.println("Invoked readConfig");
        System.out.println("Parameter:" + parameter);

    }

    @Override
    public Session login(String username, String password) {
        return auth.login(username, password);
    }

}