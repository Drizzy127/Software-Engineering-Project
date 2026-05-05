package Services;

public class SocialPost {
    private final String author;
    private final String department;
    private final String content;
    private final String time;

    public SocialPost(String author, String department, String content, String time) {
        this.author = author;
        this.department = department;
        this.content = content;
        this.time = time;
    }

    public String getAuthor() {
        return author;
    }

    public String getDepartment() {
        return department;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
