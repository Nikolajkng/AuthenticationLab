package client;

import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.Scanner;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import shared.PrinterInterface;

public class ClientMain {
    public static void main(String[] args)
            throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost", 9909, getClientSocketFactory());

        Object printerObject = registry.lookup("printer");
        if (printerObject instanceof PrinterInterface rawPrinter) {
            
            // For readability:
            String[] login = fetchUserLogin();
            String username = login[0];
            String password = login[1];
            
            ClientPrinter printer = new ClientPrinter(username, password, rawPrinter);
            printer.queue("jeg vil gerne printe noget coolio!");
        } else {
            throw new RuntimeException("Failed to lookup printer");
        }
    }

    static String[] fetchUserLogin() throws Exception {
        // Define variables to hold the username and password
        String[] result = new String[2];

        // Use of scanner file:
        File loginFile = new File("secret_printer_folder/logins.txt");
        try (Scanner sc = new Scanner(loginFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim(); 

                if (line.startsWith("username: ")) {
                    result[0] = line.substring("username: ".length());
                } else if (line.startsWith("password: ")) {
                    result[1] = line.substring("password: ".length());
                }
            }
        } catch (FileNotFoundException e) {
            throw new Exception("File not found: " + e.getMessage()); 
        }

        // Check if both values are set, otherwise throw an exception
        if (result[0] == null || result[1] == null) {
            throw new Exception("Error: Username or password not found in the file.");
        }

        return result;
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