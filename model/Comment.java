package model;
public class
Comment {
    private int id;
    private String content;
    private int userId;
    private int weiboId;

    public Comment(int id, String content, int userId, int weiboId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.weiboId = weiboId;
    }

    public static void log(String format, Object... args) {
        System.out.println(String.format(format, args));
    }

    @Override
    public String toString() {
        String s = String.format(
                "(id: %s, 内容: %s, user_id: %s)",
                this.getId(),
                this.getContent(),
                this.getUserId()
        );
        return s;
    }

    public int getId() {
        log("访问了 id");
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public int getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(int weiboId) {
        this.weiboId = weiboId;
    }
}
