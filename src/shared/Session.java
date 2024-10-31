package shared;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record Session(UUID token, Instant timestamp, String userid) implements Serializable {
    public Session(String userid) {
        this(UUID.randomUUID(), Instant.now(), userid);

    }
}
