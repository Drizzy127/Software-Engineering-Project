package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private double quizWeight = 0.20;
    private double testWeight = 0.40;
    private double assignmentWeight = 0.30;
    private double participationWeight = 0.10;

    private StringProperty courseCode;
    private StringProperty courseName;
    private StringProperty semester;
    private StringProperty section;
    private StringProperty meetingTime;
    private StringProperty room;
    private StringProperty classAverage;

    private int studentCount;
    private String firebaseId;

    //student roster
    private List<Student> students = new ArrayList<>();

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


        generateStudents(studentCount);
    }



    // student placeholder
    private void generateStudents(int count) {
        for (int i = 1; i <= count; i++) {
            students.add(new Student("Student", String.valueOf(i)));
        }
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students == null ? new ArrayList<>() : students;
        this.studentCount = this.students.size();
    }

    public void addStudent(Student student) {
        students.add(student);
        studentCount = students.size();
    }

    public void removeStudent(Student student) {
        students.remove(student);
        studentCount = students.size();
    }
    //Calculates students individual average
    public double calculateStudentWeightedAverage(Student student) {

        double quizSum = 0, testSum = 0, assignmentSum = 0, participation = 0;

        int quizCount = 0, testCount = 0, assignmentCount = 0;

        for (Grade g : student.getGrades()) {

            switch (g.getType()) {
                case "Quiz":
                    quizSum += g.getScore();
                    quizCount++;
                    break;

                case "Test":
                    testSum += g.getScore();
                    testCount++;
                    break;

                case "Assignment":
                    assignmentSum += g.getScore();
                    assignmentCount++;
                    break;

                case "Participation":
                    participation = g.getScore();
                    break;
            }
        }

        double quizAvg = quizCount == 0 ? 0 : quizSum / quizCount;
        double testAvg = testCount == 0 ? 0 : testSum / testCount;
        double assignmentAvg = assignmentCount == 0 ? 0 : assignmentSum / assignmentCount;

        return (quizAvg * quizWeight)
                + (testAvg * testWeight)
                + (assignmentAvg * assignmentWeight)
                + (participation * participationWeight);
    }
    // Calculates class average and stores it as a letter grade instead of a number
    public void updateClassAverage() {
        if (students.isEmpty()) {
            classAverage.set("N/A");
            return;
        }

        double total = 0;
        int count = 0;

        for (Student s : students) {
            double weightedAvg = calculateStudentWeightedAverage(s);

            if (weightedAvg > 0) {
                total += weightedAvg;
                count++;
            }
        }

        if (count == 0) {
            if (classAverage.get() == null || classAverage.get().isBlank() || classAverage.get().equals("0")) {
                classAverage.set("N/A");
            }
            return;
        }

        double classAvg = total / count;
        classAverage.set(toLetterGrade(classAvg));
    }

    public static String toLetterGrade(double score) {
        if (score >= 93) return "A";
        if (score >= 90) return "A-";
        if (score >= 87) return "B+";
        if (score >= 83) return "B";
        if (score >= 80) return "B-";
        if (score >= 77) return "C+";
        if (score >= 73) return "C";
        if (score >= 70) return "C-";
        if (score >= 67) return "D+";
        if (score >= 60) return "D";
        return "F";
    }

    public String getClassAverageText() {
        String value = classAverage.get();
        if (value == null || value.isBlank()) return "N/A";

        try {
            return toLetterGrade(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return value;
        }
    }
   /* private double quizWeight = 0.2;
    private double assignmentWeight = 0.3;
    private double testWeight = 0.5; */

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

    public void setQuizWeight(double quizWeight) {
        this.quizWeight = quizWeight;
    }

    public void setAssignmentWeight(double assignmentWeight) {
        this.assignmentWeight = assignmentWeight;
    }

    public void setTestWeight(double testWeight) {
        this.testWeight = testWeight;
    }


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

    public double getClassAverage() {
        try {
            return Double.parseDouble(classAverage.get());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getStudentCount() {
        return studentCount;
    }
    public void setParticipationWeight(double participationWeight) {
        this.participationWeight = participationWeight;
    }

    public double getQuizWeight() {
        return quizWeight;
    }

    public double getAssignmentWeight() {
        return assignmentWeight;
    }

    public double getTestWeight() {
        return testWeight;
    }

    public double getParticipationWeight() {
        return participationWeight;
    }
}
