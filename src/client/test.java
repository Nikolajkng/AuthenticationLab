package client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws Exception {
        // Define variables to hold the username and password
        String username = null;
        String password = null;

        // Try-with-resources for automatic resource management
        File loginFile = new File("secret_printer_folder/logins.txt");
        try (Scanner sc = new Scanner(loginFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim(); 

                if (line.startsWith("username: ")) {
                    username = line.substring("username: ".length());
                } else if (line.startsWith("password: ")) {
                    password = line.substring("password: ".length());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            return; 
        }

        // Check if both values are set, otherwise throw an exception
        if (username == null || password == null) {
            System.err.println("Error: Username or password not found in the file.");
            return;
        }

    }
}
