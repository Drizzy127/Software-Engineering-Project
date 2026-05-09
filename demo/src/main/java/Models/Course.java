package Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private double quizWeight;
    private double testWeight;
    private double assignmentWeight;
    private double participationWeight;

    private StringProperty courseCode;
    private StringProperty courseName;
    private StringProperty semester;
    private StringProperty section;
    private StringProperty meetingTime;
    private StringProperty room;
    private StringProperty classAverage;

    private int studentCount;

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

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
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
               + participation;
   }
    // Calculate's class average
    public void updateClassAverage() {
        if (students.isEmpty()) {
            classAverage.set("0");
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

        double classAvg = (count == 0) ? 0 : total / count;
        classAverage.set(String.format("%.1f", classAvg));
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
        return Double.parseDouble(classAverage.get());
    }

    public int getStudentCount() {
        return studentCount;
    }
    public double setParticipationWeight(double participantWeight) {
        return participantWeight;
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



}