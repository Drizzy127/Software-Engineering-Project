package Services;

import Models.Course;
import Models.Student;
import Models.Grade;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.InputStream;
import java.util.*;

public class FirestoreService {
    private static final String SERVICE_ACCOUNT_PATH = "/Firebase/login.json";
    private static Firestore db;

    public FirestoreService() throws Exception {
        initializeFirebase();
    }

    private static synchronized void initializeFirebase() throws Exception {
        if (db != null) return;

        if (FirebaseApp.getApps().isEmpty()) {
            InputStream serviceAccount = FirestoreService.class.getResourceAsStream(SERVICE_ACCOUNT_PATH);
            if (serviceAccount == null) {
                throw new IllegalStateException("Missing Firebase service account file at src/main/resources/Firebase/login.json");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }

        db = FirestoreClient.getFirestore();
    }

    public Firestore getDb() {
        return db;
    }

    public void saveUserDepartment(String uid, String email, String departmentId, String departmentName) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("email", email == null ? "" : email.trim().toLowerCase());
        data.put("departmentId", departmentId);
        data.put("departmentName", departmentName);

        db.collection("users").document(uid).set(data).get();
    }

    public void saveUserLogin(String uid, String email, String password, String departmentName) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("email", email.trim().toLowerCase());
        data.put("password", password);
        data.put("departmentName", departmentName);
        data.put("createdAt", FieldValue.serverTimestamp());

        db.collection("users").document(uid).set(data, SetOptions.merge()).get();
    }

    public DocumentSnapshot findUserByEmail(String email) throws Exception {
        ApiFuture<QuerySnapshot> future = db.collection("users")
                .whereEqualTo("email", email.trim().toLowerCase())
                .limit(1)
                .get();

        List<QueryDocumentSnapshot> docs = future.get().getDocuments();
        return docs.isEmpty() ? null : docs.get(0);
    }

    public String getDepartmentName(String uid) throws Exception {
        DocumentSnapshot doc = db.collection("users").document(uid).get().get();
        if (!doc.exists()) return null;
        return doc.getString("departmentName");
    }

    public List<Course> loadCourses(String uid) throws Exception {
        List<Course> courses = new ArrayList<>();
        QuerySnapshot snapshot = db.collection("users").document(uid)
                .collection("courses")
                .get()
                .get();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            Course course = new Course(
                    value(doc, "courseCode"),
                    value(doc, "courseName"),
                    value(doc, "semester"),
                    value(doc, "section"),
                    intValue(doc, "studentCount"),
                    value(doc, "meetingTime"),
                    value(doc, "room"),
                    value(doc, "classAverage")
            );
            course.setFirebaseId(doc.getId());
            course.setQuizWeight(doubleValue(doc, "quizWeight", course.getQuizWeight()));
            course.setTestWeight(doubleValue(doc, "testWeight", course.getTestWeight()));
            course.setAssignmentWeight(doubleValue(doc, "assignmentWeight", course.getAssignmentWeight()));
            course.setParticipationWeight(doubleValue(doc, "participationWeight", course.getParticipationWeight()));

            Object studentData = doc.get("students");
            if (studentData instanceof List<?>) {
                course.setStudents(parseStudents((List<?>) studentData));
            }

            courses.add(course);
        }
        return courses;
    }

    public void saveCourse(String uid, Course course) throws Exception {
        String docId = course.getFirebaseId();
        if (docId == null || docId.isBlank()) {
            docId = db.collection("users").document(uid).collection("courses").document().getId();
            course.setFirebaseId(docId);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("courseCode", course.getCourseCode());
        data.put("courseName", course.getCourseName());
        data.put("semester", course.getSemester());
        data.put("section", course.getSection());
        data.put("studentCount", course.getStudentCount());
        data.put("meetingTime", course.getMeetingTime());
        data.put("room", course.getRoom());
        data.put("classAverage", course.getClassAverageText());
        data.put("quizWeight", course.getQuizWeight());
        data.put("testWeight", course.getTestWeight());
        data.put("assignmentWeight", course.getAssignmentWeight());
        data.put("participationWeight", course.getParticipationWeight());
        data.put("students", serializeStudents(course.getStudents()));
        data.put("updatedAt", FieldValue.serverTimestamp());

        db.collection("users").document(uid).collection("courses").document(docId).set(data).get();
    }

    public void deleteCourse(String uid, Course course) throws Exception {
        if (course.getFirebaseId() == null || course.getFirebaseId().isBlank()) return;
        db.collection("users").document(uid).collection("courses").document(course.getFirebaseId()).delete().get();
    }

    public List<SocialPost> loadPosts() throws Exception {
        List<SocialPost> posts = new ArrayList<>();
        QuerySnapshot snapshot = db.collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .get();

        for (DocumentSnapshot doc : snapshot.getDocuments()) {
            posts.add(new SocialPost(
                    value(doc, "author"),
                    value(doc, "department"),
                    value(doc, "content"),
                    value(doc, "time")
            ));
        }
        return posts;
    }

    public void savePost(SocialPost post) throws Exception {
        Map<String, Object> data = new HashMap<>();
        data.put("author", post.getAuthor());
        data.put("department", post.getDepartment());
        data.put("content", post.getContent());
        data.put("time", post.getTime());
        data.put("createdAt", FieldValue.serverTimestamp());
        db.collection("posts").add(data).get();
    }


    private List<Map<String, Object>> serializeStudents(List<Student> students) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Student student : students) {
            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("firstName", student.getFirstName());
            studentMap.put("lastName", student.getLastName());

            List<Map<String, Object>> grades = new ArrayList<>();
            for (Grade grade : student.getGrades()) {
                Map<String, Object> gradeMap = new HashMap<>();
                gradeMap.put("type", grade.getType());
                gradeMap.put("score", grade.getScore());
                gradeMap.put("name", grade.getName());
                gradeMap.put("notes", grade.getNotes());
                grades.add(gradeMap);
            }
            studentMap.put("grades", grades);
            result.add(studentMap);
        }
        return result;
    }

    private List<Student> parseStudents(List<?> savedStudents) {
        List<Student> students = new ArrayList<>();
        for (Object item : savedStudents) {
            if (!(item instanceof Map<?, ?> map)) continue;
            Student student = new Student(
                    mapString(map, "firstName", "Student"),
                    mapString(map, "lastName", "")
            );

            List<Grade> grades = new ArrayList<>();
            Object gradeData = map.get("grades");
            if (gradeData instanceof List<?> gradeList) {
                for (Object gradeItem : gradeList) {
                    if (!(gradeItem instanceof Map<?, ?> gradeMap)) continue;
                    grades.add(new Grade(
                            mapString(gradeMap, "type", "Assignment"),
                            parseDouble(gradeMap.get("score"), 0),
                            mapString(gradeMap, "name", ""),
                            mapString(gradeMap, "notes", "")
                    ));
                }
            }
            student.setGrades(grades);
            students.add(student);
        }
        return students;
    }

    private String mapString(Map<?, ?> map, String key, String defaultValue) {
        Object value = map.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }

    private double doubleValue(DocumentSnapshot doc, String key, double defaultValue) {
        return parseDouble(doc.get(key), defaultValue);
    }

    private double parseDouble(Object value, double defaultValue) {
        if (value instanceof Number number) return number.doubleValue();
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private String value(DocumentSnapshot doc, String key) {        Object value = doc.get(key);
        return value == null ? "" : String.valueOf(value);
    }

    private int intValue(DocumentSnapshot doc, String key) {
        Object value = doc.get(key);
        if (value instanceof Number number) return number.intValue();
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (Exception e) {
            return 0;
        }
    }
}
