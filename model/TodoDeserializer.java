package model;

import java.util.List;

public class TodoDeserializer implements Deserializer<Todo> {
    @Override
    public Todo deserialize(List<String> modelData) {
        int id = Integer.parseInt(modelData.get(0));
        String content = modelData.get(1);
        int userId = Integer.parseInt(modelData.get(2));
        return new Todo(id, content, userId);
    }
}
