package shared;

import java.io.Serializable;

public record AccessChange(String username, String functionName) implements Serializable {

}
