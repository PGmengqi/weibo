package model;

import java.util.List;

public interface  Deserializer <T> {
    // 反序列化
    // 从字符串返回对象

    // Object deserialize(List<String> modelData);

    T deserialize(List<String> modelData);
}
