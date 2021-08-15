package service;

import model.*;

import java.util.ArrayList;

public class ServiceTodoOld {
    // public TxtStorage storage = new TxtStorage("Todo.txt", new TodoDeserializer(), 3)
    public static boolean currentUserTodo(int todoId, int currentUserId) {
        ArrayList<Todo> todos = TxtStorage.load("Todo.txt", new TodoDeserializer(), 3);
        // ArrayList<Todo> todos = storage.load()
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            if (todoId == todo.getId() && todo.getUserId() == currentUserId) {
                return true;
            }
        }
        return false;
    }

    // 换web框架之后 service/服务这一块不用动
    public static ArrayList<Todo> currentUserTodos(User currentUser) {
        // 读出 todo 数据
        ArrayList<Todo> todos = TxtStorage.load("Todo.txt", new TodoDeserializer(), 3);
        ArrayList<Todo> currentUserTodos = new ArrayList<>();
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            if (todo.getUserId() == currentUser.id) {
                currentUserTodos.add(todo);
            }
        }
        return currentUserTodos;
    }

    public static void add(String content, User currentUser) {
        // 读出所有数据
        ArrayList<Todo> todos = TxtStorage.load("Todo.txt", new TodoDeserializer(), 3);
        // 加上新数据
        int id = todos.size() + 1;
        Todo todo = new Todo(id, content, currentUser.id);
        TxtStorage.add(todo, "Todo.txt", new TodoSerializer(), new TodoDeserializer(), 3);
    }

    public static void delete(int id) {
        TxtStorage.delete(
                id,
                (model, id1) -> {return model.getId() == id;},
                "Todo.txt", new TodoDeserializer(), new TodoSerializer(), 3
        );
        //
        // // 读出所有 todo
        // ArrayList<Todo> todos = TxtStorage.load("Todo.txt", new TodoDeserializer(), 3);
        // // 删除对应 todo
        // ArrayList<Todo> todosNew = new ArrayList<Todo>();
        // for (int i = 0; i < todos.size(); i++) {
        //     Todo todo = todos.get(i);
        //     if (todo.getId() != id) {
        //         todosNew.add(todo);
        //     }
        // }
        // // 保存新数据
        // TxtStorage.save(todosNew, "Todo.txt", new TodoSerializer());
    }

    public static void update(int id, String content) {
        // 拿到所有数据
        ArrayList<Todo> todos = TxtStorage.load("Todo.txt", new TodoDeserializer(), 3);
        // 更新 id 所对应的 todo 的 content
        for (int i = 0; i < todos.size(); i++) {
            Todo todo = todos.get(i);
            if (todo.getId() == id) {
                todo.setContent(content);
            }
        }
        // 保存新数据
        TxtStorage.save(todos, "Todo.txt", new TodoSerializer());
    }
}
