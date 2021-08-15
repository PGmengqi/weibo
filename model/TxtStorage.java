package model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TxtStorage {
    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    private static void fileSave(String path, String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        try {
            // Files.write(Path.of("data", path), bytes);
            URL resource = TxtStorage.class.getResource("/data");
            String directoryPath = Path.of(resource.toURI()).toString();
            log("TxtStorage resource path<%s>", directoryPath);
            Files.write(Path.of(directoryPath, path), bytes);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String fileLoad(String path) {
        byte[] bytes;
        try {
            // bytes = Files.readAllBytes(Path.of("data", path));
            URL resource = TxtStorage.class.getResource("/data");
            String directoryPath = Path.of(resource.toURI()).toString();
            log("TxtStorage resource path<%s>", directoryPath);
            bytes = Files.readAllBytes(Path.of(directoryPath, path));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static <T> void add(T model, String filename, Serializer<T> serializer, Deserializer<T> deserializer, int filedCount) {
        ArrayList<T> all = load(filename, deserializer, filedCount);
        all.add(model);
        save(all, filename, serializer);
    }

    public static <T> void save(ArrayList<T> all, String filename, Serializer<T> serializer) {
        String content = "";
        for (int i = 0; i < all.size(); i++) {
            T m = all.get(i);
            String s = serializer.serialize(m);
            // String s = String.format("%s\n%s\n", m.author, m.content);
            content = content + s;
        }
        log("save all <%s>", content);

        fileSave(filename, content);
    }

    public static <T> ArrayList<T> load(String filename, Deserializer<T> deserializer, int filedCount) {
        String data = fileLoad(filename);
        log("load data: <%s>", data);
        ArrayList<T> all = new ArrayList<>();

        if (data.length() > 0) {
            List<String> lines = Arrays.asList(data.split("\n"));
            log("load lines %s", lines.size());
            for (int i = 0; i < lines.size(); i = i + filedCount) {
                // String author = lines[i];
                // String content = lines[i + 1];
                // T m = new T(author, content);
                List<String> modelData = lines.subList(i, i + filedCount);
                T m = deserializer.deserialize(modelData);
                all.add(m);
            }
        }
        return all;
    }

    public static <T> void delete(int id, Compare<T> compare, String filename, Deserializer<T> deserializer, Serializer<T> serializer, int filedCount) {
        ArrayList<T> models = TxtStorage.load(filename, deserializer, filedCount);
        ArrayList<T> modelsNew = new ArrayList<T>();
        for (int i = 0; i < models.size(); i++) {
            T model = models.get(i);
            if (!compare.equal(model, id)) {
                modelsNew.add(model);
            }
        }
        // 保存新数据
        TxtStorage.save(modelsNew, filename, serializer);
    }


}
