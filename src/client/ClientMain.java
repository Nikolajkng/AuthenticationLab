package client;

import java.io.IOException;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import shared.AccessChange;
import shared.ClientSession;
import shared.PrinterInterface;
import shared.RoleChange;

public class ClientMain {
    public static void main(String[] args)
            throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 9909, getClientSocketFactory());

        Object printerObject = registry.lookup("printer");
        if (printerObject instanceof PrinterInterface rawPrinter) {

            // rawPrinter.register("Bob", "123");
            // For readability:
            String username = "Cecilia";
            String password = "123";

            ClientPrinter printer = new ClientPrinter(username, password, rawPrinter);

            printer.print("Jeeeee", "hawe");

            // printer.updateRoles(
            // new RoleChange[] { new RoleChange("power-user", "Ida"), new
            // RoleChange("ordinary", "Henry") },
            // new RoleChange[] { new RoleChange("technician", "Bob") });

            // printer.updateAccessLists(
            // new AccessChange[] {
            // new AccessChange("queue", "Ida"),
            // new AccessChange("restart", "Ida"),
            // new AccessChange("topQueue", "Ida"),
            // new AccessChange("print", "Ida"),
            // new AccessChange("queue", "Henry"),
            // new AccessChange("print", "Henry") },
            // new AccessChange[] {
            // new AccessChange("restart", "Bob"),
            // new AccessChange("setConfig", "Bob"),
            // new AccessChange("start", "Bob"),
            // new AccessChange("stop", "Bob"),
            // new AccessChange("readCondig", "Bob"),
            // });
        } else {
            throw new RuntimeException("Failed to lookup printer");
        }
    }

    static RMIClientSocketFactory getClientSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustManagers = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                            throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                            throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        // Create SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());

        SocketFactory socketFactory = sslContext.getSocketFactory();

        return new RMIClientSocketFactory() {
            @Override
            public Socket createSocket(String host, int port) throws IOException {
                return socketFactory.createSocket(host, port);
            }
        };
    }
}