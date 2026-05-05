package Services;

import Models.Course;
import java.util.ArrayList;
import java.util.List;

public class AppState {
    private static String loggedInEmail = "professor@farmingdale.edu";
    private static final List<Course> courses = new ArrayList<>();
    private static final List<SocialPost> posts = new ArrayList<>();

    static {
        courses.add(new Course("CSC 101", "Intro to CS", "Fall 2026", "Section 01", 35, "Mon/Wed 10:00AM", "Whitman 204", "C"));
        courses.add(new Course("CSC 171", "Database Management", "Fall 2026", "Section 032", 25, "Mon/Wed 12:15PM", "Whitman 220", "A+"));
        courses.add(new Course("CSC 101", "Intro to CS", "Fall 2026", "Section 01", 35, "Mon/Wed 10:00AM", "Whitman 204", "C"));

        posts.add(new SocialPost("Prof U", "Comp Sys", "Greetings fellow teachers I am hosting a dinner party tonight in whitman at 8 pm.", "1:03pm"));
        posts.add(new SocialPost("Prof U", "Comp Sys", "Greetings fellow teachers I am hosting a dinner party tonight in whitman at 8 pm.", "1:03pm"));
        posts.add(new SocialPost("Prof U", "Comp Sys", "Greetings fellow teachers I am hosting a dinner party tonight in whitman at 8 pm.", "1:03pm"));
    }

    public static String getLoggedInEmail() {
        return loggedInEmail;
    }

    public static void setLoggedInEmail(String email) {
        if (email != null && !email.isBlank()) {
            loggedInEmail = email.trim();
        }
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static void addCourse(Course course) {
        courses.add(course);
    }

    public static void removeCourse(Course course) {
        courses.remove(course);
    }

    public static List<SocialPost> getPosts() {
        return posts;
    }

    public static void addPost(SocialPost post) {
        posts.add(0, post);
    }
}
