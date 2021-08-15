package model;

public class User {
    public int id;
    public String username;
    public String password;
    public UserRole role;
    public String salt;

    public User(int id, String username, String password, UserRole role, String salt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.salt = salt;
    }

    @Override
    public String toString() {
        String s = String.format(
                "(用户名: %s, 密码: %s, 角色 %s 盐 %s)",
                this.username,
                this.password,
                this.role,
                this.salt
        );
        return s;
    }
}
