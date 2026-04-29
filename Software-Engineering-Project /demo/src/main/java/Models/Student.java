package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private final StringProperty name;
    private final StringProperty studentId;
    private final StringProperty grade;
    private final StringProperty status;

    public Student(String name) {
        this(name, "", "", "Active");
    }

    public Student(String name, String studentId, String grade, String status) {
        this.name = new SimpleStringProperty(name);
        this.studentId = new SimpleStringProperty(studentId);
        this.grade = new SimpleStringProperty(grade);
        this.status = new SimpleStringProperty(status);
    }

    public String getName() { return name.get(); }
    public String getStudentId() { return studentId.get(); }
    public String getGrade() { return grade.get(); }
    public String getStatus() { return status.get(); }

    public StringProperty nameProperty() { return name; }
    public StringProperty studentIdProperty() { return studentId; }
    public StringProperty gradeProperty() { return grade; }
    public StringProperty statusProperty() { return status; }

    @Override
    public String toString() { return name.get(); }
}
