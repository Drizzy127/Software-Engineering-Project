package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {

    private StringProperty courseCode;
    private StringProperty courseName;

    private StringProperty semester;
    private StringProperty section;
    private StringProperty meetingTime;
    private StringProperty room;
    private StringProperty classAverage;

    private int studentCount;

    public Course(String courseCode,
                  String courseName,
                  String semester,
                  String section,
                  int studentCount,
                  String meetingTime,
                  String room,
                  String classAverage) {

        this.courseCode = new SimpleStringProperty(courseCode);
        this.courseName = new SimpleStringProperty(courseName);
        this.semester = new SimpleStringProperty(semester);
        this.section = new SimpleStringProperty(section);
        this.meetingTime = new SimpleStringProperty(meetingTime);
        this.room = new SimpleStringProperty(room);
        this.classAverage = new SimpleStringProperty(classAverage);

        this.studentCount = studentCount;
    }

    // ===== JavaFX properties (for TableView) =====
    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    public StringProperty sectionProperty() {
        return section;
    }

    public StringProperty meetingTimeProperty() {
        return meetingTime;
    }

    public StringProperty roomProperty() {
        return room;
    }

    public StringProperty classAverageProperty() {
        return classAverage;
    }

    // ===== Getters =====
    public String getCourseCode() {
        return courseCode.get();
    }

    public String getCourseName() {
        return courseName.get();
    }

    public String getSemester() {
        return semester.get();
    }

    public String getSection() {
        return section.get();
    }

    public String getMeetingTime() {
        return meetingTime.get();
    }

    public String getRoom() {
        return room.get();
    }

    public String getClassAverage() {
        return classAverage.get();
    }

    public int getStudentCount() {
        return studentCount;
    }
}