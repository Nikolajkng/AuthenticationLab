package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import shared.PrinterInterface;
import shared.ClientSession;

public class Printer extends UnicastRemoteObject implements PrinterInterface {

    Auth auth;

    protected Printer(Auth auth) throws RemoteException {
        super();
        this.auth = auth;
    }

    // Overwrite prototype methods with actual functionality:
    public void print(ClientSession session, String filename, String printer) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked print");
        System.out.println("Filename of " + printer + ": " + filename);
    }

    public void queue(ClientSession session, String printer) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked queue");
        System.out.println("Printer: " + printer);

    }

    public void topQueue(ClientSession session, String printer, int job) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked topQueue");
        System.out.println("Printer: " + printer);
        System.out.println("Job: " + job);

    }

    public void start(ClientSession session ) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked start");

    }

    public void stop(ClientSession session) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked stop");

    }

    public void restart(ClientSession session) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked restart");

    }

    public void status(ClientSession session, String printer) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked status");
        System.out.println("Printer: " + printer);
    }

    public void setConfig(ClientSession session, String parameter, String value) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked setConfig");
        System.out.println("Parameter: " + parameter);
        System.out.println("Value: " + value);

    }

    public void readConfig(ClientSession session, String parameter) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked readConfig");
        System.out.println("Parameter:" + parameter);

    }

    @Override
    public ClientSession login(String username, String password) {
        return auth.login(username, password);
    }

    @Override
    public ClientSession register(String username, String password) throws RemoteException {
        auth.register(username, password);
        return auth.login(username, password);
    }

}