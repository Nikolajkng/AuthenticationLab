package server;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import shared.Session;

class Auth {

    Connection connection;
    HashMap<UUID, Session> sessions = new HashMap<>();

    Auth(Connection connection) {
        this.connection = connection;
    }

    public synchronized Session login(String username, String password) {

        try {
            PreparedStatement stmt = connection
                    .prepareStatement(
                            "select userid from users where username = ? and md5(concat(?,pass_salt)) = pass_hash");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();
            if (result.first()) {
                String userid = result.getString("userid");
                Session session = new Session(userid);
                sessions.put(session.token(), session);
                return session;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Invalid username or password");
    }

    public synchronized void authenticate(Session clientSession) {
        Session session = sessions.get(clientSession.token());
        // Ignore the info in clientSession. It is not to be trusted
        if (session == null) {
            // invalid session key
            throw new RuntimeException("Not authenticated");
        }

        if (Duration.between(session.timestamp(), Instant.now()).getSeconds() > 60 * 10) {
            // Remove from hashmap and return false;
            sessions.remove(session.token());
            throw new RuntimeException("Not authenticated");
        }
    }

    public synchronized boolean register(String username, String password) {
        String salt = "" + new SecureRandom().nextInt();
        try {
            var stmt = connection
                    .prepareStatement(
                            "insert into users (username, pass_hash,pass_salt) values (?,md5(concat(?,?)),?)");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, salt);
            stmt.setString(4, salt);
            int rows = stmt.executeUpdate();
            if (rows == 1) {
                // Successfully inserted 1 user
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;

    }
}