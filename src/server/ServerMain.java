package server;

import java.io.FileInputStream;
import java.io.IOException;
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

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

public class ServerMain {
    public static void main(String[] args) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mariadb://jensogkarsten.site:3306/Printer",
                    "dtu", "kage123");

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

    static final char[] CERT_PASSWORD = "jsDR2Zbm".toCharArray();

    static SslRMIServerSocketFactory getServerSocketFactory()
            throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
            UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream keyStream = new FileInputStream("secret_printer_folder/keystore.jks")) {
            keyStore.load(keyStream, CERT_PASSWORD);
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, CERT_PASSWORD);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        return new SslRMIServerSocketFactory(sslContext, null, null, false);
    }
}
