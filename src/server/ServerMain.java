package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerMain {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mariadb://jensogkarsten.site:3306/Printer",
                "dtu", "kage123")) {

            Auth auth = new Auth(connection);

            

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
