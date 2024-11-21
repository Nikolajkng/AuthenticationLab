package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        try {
            // For readability:
            String[] databaseInfo = fetchDatabaseLogin();
            String DB_URL = databaseInfo[0];
            String DB_username = databaseInfo[1];
            String DB_password = databaseInfo[2];
            Properties p = new Properties();
            p.setProperty("user", DB_username);
            p.setProperty("password", DB_password);
            p.setProperty("sslMode", "trust");
            Connection connection = DriverManager.getConnection(DB_URL, p);

            connection.setAutoCommit(true);

            Auth auth = new Auth(connection);

            Printer printer = new Printer(auth);

            Registry registry = LocateRegistry.createRegistry(9909, new SslRMIClientSocketFactory(),
                    getServerSocketFactory());

            registry.rebind("printer", printer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static String[] fetchDatabaseLogin() throws Exception {
        // Define variables to hold the database "url, username, password"
        String[] result = new String[3];

        // Use of scanner file:
        File loginFile = new File("secret_printer_folder/database_info.txt");
        try (Scanner sc = new Scanner(loginFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();

                if (line.startsWith("url: ")) {
                    result[0] = line.substring("url: ".length());
                } else if (line.startsWith("username: ")) {
                    result[1] = line.substring("username: ".length());
                } else if (line.startsWith("password: ")) {
                    result[2] = line.substring("password: ".length());
                }
            }
        } catch (FileNotFoundException e) {
            throw new Exception("File not found: " + e.getMessage());
        }

        // Check if both values are set, otherwise throw an exception
        if (result[0] == null || result[1] == null) {
            throw new Exception("Error: URL, username or password not found in the file.");
        }

        return result;
    }

    static final char[] loadCertPass() {
        try {
            return Files.readString(Path.of("secret_printer_folder/cert_pass.txt")).toCharArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static SslRMIServerSocketFactory getServerSocketFactory()
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        char[] certPass = loadCertPass();
        try (FileInputStream keyStream = new FileInputStream("secret_printer_folder/keystore.jks")) {
            keyStore.load(keyStream, certPass);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, certPass);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        return new SslRMIServerSocketFactory(sslContext, null, null, false);
    }
}
