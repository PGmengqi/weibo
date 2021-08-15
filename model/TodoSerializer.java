package model;

public class TodoSerializer implements Serializer<Todo> {
    @Override
    public String serialize(Todo todo) {
        String s = String.format(
                "%s\n%s\n%s\n",
                todo.getId(), todo.getContent(), todo.getUserId()
        );
        return s;
    }
}
