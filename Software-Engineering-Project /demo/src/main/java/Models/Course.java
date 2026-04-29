package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {

    private StringProperty courseCode;
    private StringProperty courseName;

    public Course(String code, String name) {
        this.courseCode = new SimpleStringProperty(code);
        this.courseName = new SimpleStringProperty(name);
    }

    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public String getCourseName() {
        return courseName.get();
    }
}