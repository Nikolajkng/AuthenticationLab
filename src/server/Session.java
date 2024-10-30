package server;

import java.util.Date;
import java.util.UUID;

public record Session(UUID token, Date timestamp, String userid) {
    public Session(String userid) {
        this(UUID.randomUUID(), new Date(), userid);

    }
}
