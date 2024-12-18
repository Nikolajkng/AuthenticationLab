package client;

import java.rmi.RemoteException;

import shared.PrinterInterface;
import shared.RoleChange;
import shared.AccessChange;
import shared.ClientSession;

public class ClientPrinter {
    PrinterInterface printer;
    ClientSession session;

    ClientPrinter(String username, String password, PrinterInterface printer) throws RemoteException {
        this.printer = printer;
        session = printer.login(username, password);
    }

    // Declaring methods prototypes:
    public void print(String filename, String printer) throws RemoteException {
        this.printer.print(session, filename, printer);
    }

    public void queue(String printer) throws RemoteException {
        this.printer.queue(session, printer);
    }

    public void topQueue(String printer, int job) throws RemoteException {
        this.printer.topQueue(session, printer, job);
    }

    public void start() throws RemoteException {
        this.printer.start(session);
    }

    public void stop() throws RemoteException {
        this.printer.stop(session);
    }

    public void restart() throws RemoteException {
        this.printer.restart(session);
    }

    public void status(String printer) throws RemoteException {
        this.printer.status(session, printer);
    }

    public void setConfig(String parameter, String value) throws RemoteException {
        this.printer.setConfig(session, parameter, value);
    }

    public void readConfig(String parameter) throws RemoteException {
        this.printer.readConfig(session, parameter);
    }

    public void updateRoles(RoleChange[] added, RoleChange[] removed) throws RemoteException {
        this.printer.updateRoles(session, added, removed);
    }

    public void updateAccessLists(AccessChange[] added, AccessChange[] removed) throws RemoteException {
        this.printer.updateAccessLists(session, added, removed);
    }
}
