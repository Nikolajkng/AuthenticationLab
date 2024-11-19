package shared;

import java.io.Serializable;

public record RoleChange(String username, String roleName) implements Serializable {

}
