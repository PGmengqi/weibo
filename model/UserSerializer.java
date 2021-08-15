package model;

public class UserSerializer implements Serializer<User> {
    @Override
    public String serialize(User user) {
        String s = String.format(
                "%s\n%s\n%s\n%s\n%s\n",
                user.id, user.username, user.password, user.role, user.salt
        );
        return s;
    }
}
