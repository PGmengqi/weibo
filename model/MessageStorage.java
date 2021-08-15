package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class MessageStorage {

    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    private static void fileSave(String path, String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        try {
            Files.write(Path.of(path), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String fileLoad(String path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void add(Message message) {
        ArrayList<Message> all = load();
        all.add(message);

        String content = "";
        for (int i = 0; i < all.size(); i++) {
            Message m = all.get(i);
            String s = String.format("%s\n%s\n", m.author, m.content);
            content = content + s;
        }
        log("save all <%s>", content);
        String filename = "data/Message.txt";

        fileSave(filename, content);
    }

    public static ArrayList<Message> load() {
        String filename = "data/Message.txt";
        String data = fileLoad(filename);
        log("load data: <%s>", data);
        ArrayList<Message> all = new ArrayList<>();

        if (data.length() > 0) {
            String[] lines = data.split("\n");
            log("load lines %s", lines.length);


            for (int i = 0; i < lines.length; i = i + 2) {
                String author = lines[i];
                String content = lines[i + 1];
                Message m = new Message(author, content);
                all.add(m);
            }
        }
        return all;
    }
}
