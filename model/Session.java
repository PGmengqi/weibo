package model;

import java.util.UUID;

public class Session {
    public String id;
    public String username;

    public Session (String id, String username) {
        this.id = id;
        this.username = username;
    }
}
