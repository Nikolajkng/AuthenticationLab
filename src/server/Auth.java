package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

class Auth {

    Connection connection;
    HashMap<UUID, Session> sessions = new HashMap<>();

    Auth(Connection connection) {
        this.connection = connection;
    }

    String login(String username, String password) {

        try {
            PreparedStatement stmt = connection
                    .prepareStatement("select userid from users where username = ? and kdf(?,pass_salt) = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();
            if (result.first()) {
                String userid = result.getString("userid");
                Session session = new Session(userid);
                sessions.put(session.token(), session);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    boolean authenticate(Session clientSession) {
        Session session = sessions.get(clientSession.token());
        // Ignore the info in clientSession. It is not to be trusted
        if (session == null) {
            // invalid session key
            return false;
        }
        return true;

        // Duration d = Duration.ofMinutes(10);
        // var kage = Instant.now() + d;

        // // Check expited
        // if (session.timestamp().before(new Date() + d)) {

        // }
    }
}