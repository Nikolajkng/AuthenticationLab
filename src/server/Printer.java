package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import shared.PrinterInterface;
import shared.Session;

public class Printer extends UnicastRemoteObject implements PrinterInterface {

    Auth auth;

    protected Printer(Auth auth) throws RemoteException {
        super();
        this.auth = auth;
    }

    // Overwrite prototype methods with actual functionality:
    public void print(Session session, String filename, String printer) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked print");
        System.out.println("Filename of " + printer + ": " + filename);
    }

    public void queue(Session session, String printer) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked queue");
        System.out.println("Printer: " + printer);

    }

    public void topQueue(Session session, String printer, int job) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked topQueue");
        System.out.println("Printer: " + printer);
        System.out.println("Job: " + job);

    }

    public void start(Session session ) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked start");

    }

    public void stop(Session session) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked stop");

    }

    public void restart(Session session) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked restart");

    }

    public void status(Session session, String printer) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked status");
        System.out.println("Printer: " + printer);
    }

    public void setConfig(Session session, String parameter, String value) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked setConfig");
        System.out.println("Parameter: " + parameter);
        System.out.println("Value: " + value);

    }

    public void readConfig(Session session, String parameter) throws RemoteException {
        auth.authenticate(session);
        System.out.println("Invoked readConfig");
        System.out.println("Parameter:" + parameter);

    }

    @Override
    public Session login(String username, String password) {
        return auth.login(username, password);
    }

    @Override
    public Session register(String username, String password) throws RemoteException {
        auth.register(username, password);
        return auth.login(username, password);
    }

}