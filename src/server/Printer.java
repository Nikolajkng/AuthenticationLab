package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import shared.PrinterInterface;
import shared.RoleChange;
import shared.AccessChange;
import shared.ClientSession;

public class Printer extends UnicastRemoteObject implements PrinterInterface {

    Auth auth;

    protected Printer(Auth auth) throws RemoteException {
        super();
        this.auth = auth;
    }

    // Overwrite prototype methods with actual functionality:
    public void print(ClientSession session, String filename, String printer) throws RemoteException {
        auth.checkAccessControlPolicy("print", auth.authenticate(session));
        System.out.println("Invoked print");
        System.out.println("Filename of " + printer + ": " + filename);
    }

    public void queue(ClientSession session, String printer) throws RemoteException {
        auth.checkAccessControlPolicy("queue", auth.authenticate(session));

        System.out.println("Invoked queue");
        System.out.println("Printer: " + printer);

    }

    public void topQueue(ClientSession session, String printer, int job) throws RemoteException {
        auth.checkAccessControlPolicy("topQueue", auth.authenticate(session));
        System.out.println("Invoked topQueue");
        System.out.println("Printer: " + printer);
        System.out.println("Job: " + job);

    }

    public void start(ClientSession session) throws RemoteException {
        auth.checkAccessControlPolicy("start", auth.authenticate(session));

        System.out.println("Invoked start");

    }

    public void stop(ClientSession session) throws RemoteException {
        auth.checkAccessControlPolicy("stop", auth.authenticate(session));
        System.out.println("Invoked stop");

    }

    public void restart(ClientSession session) throws RemoteException {
        auth.checkAccessControlPolicy("restart", auth.authenticate(session));
        System.out.println("Invoked restart");

    }

    public void status(ClientSession session, String printer) throws RemoteException {
        auth.checkAccessControlPolicy("status", auth.authenticate(session));
        System.out.println("Invoked status");
        System.out.println("Printer: " + printer);
    }

    public void setConfig(ClientSession session, String parameter, String value) throws RemoteException {
        auth.checkAccessControlPolicy("setConfig", auth.authenticate(session));

        System.out.println("Invoked setConfig");
        System.out.println("Parameter: " + parameter);
        System.out.println("Value: " + value);

    }

    public void readConfig(ClientSession session, String parameter) throws RemoteException {
        auth.checkAccessControlPolicy("readConfig", auth.authenticate(session));
        System.out.println("Invoked readConfig");
        System.out.println("Parameter:" + parameter);

    }

    @Override
    public void updateRoles(ClientSession session, RoleChange[] added, RoleChange[] removed) {
        auth.checkAccessControlPolicy("updateRoles", auth.authenticate(session));
        auth.updateRoles(added, removed);

        System.out.println("Updated roles:");
        System.out.println("Added: " + Arrays.toString(added));
        System.out.println("Removed: " + Arrays.toString(removed));
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

    @Override
    public void updateAccessLists(ClientSession session, AccessChange[] added, AccessChange[] removed)
            throws RemoteException {

        auth.checkAccessControlPolicy("updateRoles", auth.authenticate(session));
        auth.updateAccessLists(added, removed);

        System.out.println("Updated roles:");
        System.out.println("Added: " + Arrays.toString(added));
        System.out.println("Removed: " + Arrays.toString(removed));
    }

}