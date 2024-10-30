package shared;

import java.time.Instant;
import java.util.UUID;

public record Session(UUID token, Instant timestamp, String userid) {
    public Session(String userid) {
        this(UUID.randomUUID(), Instant.now(), userid);

    }
}
