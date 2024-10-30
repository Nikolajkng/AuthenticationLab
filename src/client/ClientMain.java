package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.PrinterInterface;

public class ClientMain {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(9909);

        Object printerObject = registry.lookup("printer");
        if (printerObject instanceof PrinterInterface printer) {
            printer.queue("jeg vil gerne printe noget coolio!");
        } else {
            throw new RuntimeException("Failed to lookup printer");
        }

    }
}
// Dengang kørte database og server ikke på samme computer.
