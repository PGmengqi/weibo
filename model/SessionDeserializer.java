package model;

import java.util.List;

public class SessionDeserializer implements Deserializer<Session> {
    @Override
    public Session deserialize(List<String> modelData) {
        String id = modelData.get(0);
        String content = modelData.get(1);
        return new Session(id, content);
    }
}
