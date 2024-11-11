package server;

import shared.ClientSession;

public record ServerSession(String userID, ClientSession clientsession) {
    public ServerSession(String userID) {
        this(userID, new ClientSession());
    }
}
