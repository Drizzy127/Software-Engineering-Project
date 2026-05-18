package Services;

import Models.Course;
import java.util.ArrayList;
import java.util.List;

public class AppState {
    private static String loggedInUid;
    private static String loggedInEmail = "";
    private static String loggedInDepartment = "General";
    private static final List<Course> courses = new ArrayList<>();
    private static final List<SocialPost> posts = new ArrayList<>();

    public static String getLoggedInUid() {
        return loggedInUid;
    }

    public static void setLoggedInUid(String uid) {
        loggedInUid = uid;
    }

    public static String getLoggedInEmail() {
        return loggedInEmail;
    }

    public static void setLoggedInEmail(String email) {
        loggedInEmail = email == null ? "" : email.trim();
    }

    public static String getLoggedInDepartment() {
        return loggedInDepartment;
    }

    public static void setLoggedInDepartment(String department) {
        loggedInDepartment = (department == null || department.isBlank()) ? "General" : department.trim();
    }

    public static List<Course> getCourses() {
        return courses;
    }

    public static void setCourses(List<Course> newCourses) {
        courses.clear();
        if (newCourses != null) courses.addAll(newCourses);
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

    public static void setPosts(List<SocialPost> newPosts) {
        posts.clear();
        if (newPosts != null) posts.addAll(newPosts);
    }

    public static void addPost(SocialPost post) {
        posts.add(0, post);
    }
}

