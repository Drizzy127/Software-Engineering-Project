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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public void setName(String fullName) {

        String[] parts = fullName.trim().split(" ", 2);

        if (parts.length > 0) {
            this.firstName = parts[0];
        }

        if (parts.length > 1) {
            this.lastName = parts[1];
        } else {
            this.lastName = "";
        }
    }

    public void addGrade(String type, double score) {
        grades.add(new Grade(type, score));
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public double getAverage() {
        return 0;
    }

    public double getRawAverage() {

        if (grades.isEmpty()) return 0;

        double total = 0;

        for (Grade g : grades) {
            total += g.getScore();
        }

        return total / grades.size();
    }

    public void setParticipation(double score) {
        grades.removeIf(g -> g.getType().equals("Participation"));
        grades.add(new Grade("Participation", score));
    }
}