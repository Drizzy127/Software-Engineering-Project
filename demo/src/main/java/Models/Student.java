package Models;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private String firstName;
    private String lastName;
    private List<Grade> grades = new ArrayList<>();

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getName() { return firstName + " " + lastName; }

    public void setName(String fullName) {
        String[] parts = fullName.trim().split(" ", 2);
        if (parts.length > 0) this.firstName = parts[0];
        this.lastName = parts.length > 1 ? parts[1] : "";
    }

    public void addGrade(String type, double score) {
        grades.add(new Grade(type, score));
    }

    public void addGrade(String type, double score, String name, String notes) {
        grades.add(new Grade(type, score, name, notes));
    }

    public List<Grade> getGrades() { return grades; }
    public void setGrades(List<Grade> grades) {
        this.grades = grades == null ? new ArrayList<>() : grades;
    }

    public double getRawAverage() {
        if (grades.isEmpty()) return 0;
        double total = 0;
        for (Grade g : grades) total += g.getScore();
        return total / grades.size();
    }

    public void setParticipation(double score) {
        setParticipation(score, "Participation", "");
    }

    public void setParticipation(double score, String name, String notes) {
        grades.removeIf(g -> g.getType().equals("Participation"));
        grades.add(new Grade("Participation", score, name, notes));
    }
}
