package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerMain {
    public static void main(String[] args) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://jensogkarsten.site:3306/Printer",
                "dtu", "kage123")) {

            Auth auth = new Auth(connection);

            Printer printer = new Printer(auth);

            Registry registry = LocateRegistry.createRegistry(9909);
            // app.listen()

            registry.rebind("printer", printer);
            //app.get("/printer", (res,res) => printer.whatever)

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
