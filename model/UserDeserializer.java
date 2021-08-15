package model;

import java.util.List;

public class UserDeserializer implements Deserializer<User> {
    @Override
    public User deserialize(List<String> modelData) {
        int id = Integer.parseInt(modelData.get(0));
        String username = modelData.get(1);
        String password = modelData.get(2);
        UserRole role = UserRole.valueOf(modelData.get(3));
        String salt = modelData.get(4);
        return new User(id, username, password, role, salt);
    }
}
