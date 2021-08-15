package model;

public class SessionSerializer implements Serializer<Session> {
    @Override
    public String serialize(Session session) {
        String s = String.format(
                "%s\n%s\n",
                session.id, session.username
        );
        return s;
    }
}
