package model;

public interface Serializer<T> {
    // 序列化
    // 从对象返回字符串
    String serialize(T o);
}
