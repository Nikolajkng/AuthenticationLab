package shared;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record ClientSession(UUID token, Instant timestamp) implements Serializable {
    public ClientSession() {
        this(UUID.randomUUID(), Instant.now());
    }
}
