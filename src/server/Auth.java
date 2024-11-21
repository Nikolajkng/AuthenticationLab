package server;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import shared.AccessChange;
import shared.ClientSession;
import shared.RoleChange;

class Auth {

    Connection connection;
    HashMap<UUID, ServerSession> sessions = new HashMap<>();

    Auth(Connection connection) {
        this.connection = connection;
    }

    public synchronized ClientSession login(String username, String password) {
        try {
            PreparedStatement stmt = connection
                    .prepareStatement(
                            "select userid from users where username = ? and md5(concat(?,pass_salt)) = pass_hash");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();
            if (result.first()) {
                String userid = result.getString("userid");
                ServerSession session = new ServerSession(userid);
                sessions.put(session.clientsession().token(), session);
                return session.clientsession();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Failed login with username: " + username);
        throw new RuntimeException("Invalid username or password");
    }

    public synchronized String authenticate(ClientSession clientSession) {
        try {
            PreparedStatement sslCheck = connection.prepareStatement("show variables like '%ssl%'");
            ResultSet data = sslCheck.executeQuery();
            while (data.next()) {
                System.out.println(data.getString("Variable_name") + ": " + data.getString("Value"));
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ServerSession sSession = sessions.get(clientSession.token());
        // Ignore the info in clientSession. It is not to be trusted
        if (sSession == null) {
            // invalid session key
            throw new RuntimeException("Not authenticated");
        }
        ClientSession session = sSession.clientsession();

        if (Duration.between(session.timestamp(), Instant.now()).getSeconds() > 60 * 10) {
            // Remove from hashmap and return false;
            sessions.remove(session.token());
            throw new RuntimeException("Session expired");
        }
        return sSession.userID();
    }

    public synchronized void register(String username, String password) {
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
                return;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Username already taken");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Internal server error");
        }
    }

    enum AccessControlMethod {
        RoleBased,
        ListBased
    }

    public AccessControlMethod accessControlMethod = AccessControlMethod.RoleBased;

    public void checkAccessControlPolicy(String functionName, String userID) {
        switch (accessControlMethod) {
            case RoleBased:
                checkAccessControlPolicyRoleBased(functionName, userID);
                break;
            case ListBased:
                checkAccessControlPolicyListBased(functionName, userID);
                break;
        }
    }

    private void checkAccessControlPolicyListBased(String functionName, String userID) {
        try {
            PreparedStatement stmt = connection
                    .prepareStatement("select * from user_func_access where userid = ? and f_name = ?");
            stmt.setString(1, userID);
            stmt.setString(2, functionName);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Internal server error");
        }
        throw new RuntimeException("User not authorized to call function: " + functionName);
    }

    private void checkAccessControlPolicyRoleBased(String functionName, String userID) {
        try {
            PreparedStatement stmt = connection.prepareStatement("select * from user_roles where userid = ?");
            stmt.setString(1, userID);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                String role_name = res.getString("r_name");
                if (checkRolesAccess(functionName, role_name)) {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Interval server error");
        }

        throw new RuntimeException("User not authorized to call function: " + functionName);
    }

    private boolean checkRolesAccess(String functionName, String role) throws SQLException {
        PreparedStatement stmt = connection
                .prepareStatement("select * from func_access where r_name = ? and f_name = ?");
        stmt.setString(1, role);
        stmt.setString(2, functionName);
        ResultSet res = stmt.executeQuery();
        if (res.next()) {
            return true;
        }
        // Find subroles
        PreparedStatement stmtRoles = connection.prepareStatement("select * from role_extentions where upper_role = ?");
        stmtRoles.setString(1, role);
        ResultSet roleRes = stmtRoles.executeQuery();

        while (roleRes.next()) {
            if (checkRolesAccess(functionName, roleRes.getString("lower_role"))) {
                return true;
            }
        }
        return false;
    }

    public synchronized void updateAccessLists(AccessChange[] added, AccessChange[] removes) {
        for (AccessChange add : added) {
            try {
                PreparedStatement stmt = connection
                        .prepareStatement(
                                "insert into user_func_access (f_name, userid )values (?,(select userid from users where username = ?))");

                stmt.setString(1, add.functionName());
                stmt.setString(2, add.username());
                stmt.executeUpdate();

            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("User access duplicate ignored: " + add);
                continue;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Internval server error");
            }
        }
        for (AccessChange rem : removes) {
            try {
                PreparedStatement stmt = connection
                        .prepareStatement(
                                "delete from user_func_access where f_name = ? and userid in (select userid from users where username = ?)");

                stmt.setString(1, rem.functionName());
                stmt.setString(2, rem.username());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Internval server error");
            }
        }

    }

    public synchronized void updateRoles(RoleChange[] added, RoleChange[] removes) {
        // Has to be syncronzied to avoid role update race condition

        for (RoleChange add : added) {
            try {
                PreparedStatement stmt = connection
                        .prepareStatement(
                                "insert into user_roles (r_name, userid )values (?,(select userid from users where username = ?))");

                stmt.setString(1, add.roleName());
                stmt.setString(2, add.username());
                stmt.executeUpdate();

            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("User role duplicate ignored: " + add);
                continue;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Internval server error");
            }
        }
        for (RoleChange rem : removes) {
            try {
                // Fetch current roles
                PreparedStatement stmt = connection
                        .prepareStatement(
                                "delete from user_roles where r_name = ? and userid in (select userid from users where username = ?)");

                stmt.setString(1, rem.roleName());
                stmt.setString(2, rem.username());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Internval server error");
            }
        }
    }
}