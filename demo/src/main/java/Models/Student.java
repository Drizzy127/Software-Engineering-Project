package Models;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private String firstName;
    private String lastName;

    // stores structured grades
    private List<Grade> grades = new ArrayList<>();

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    // includes grade type
    public void addGrade(String type, double score) {
        grades.add(new Grade(type, score));
    }

    public List<Grade> getGrades() {
        return grades;
    }


    public double getAverage() {
        return 0; // kept for compatibility, but NOT used anymore
    }

    // used by Course ONLY (safe + clean)
    public double getRawAverage() {

        if (grades.isEmpty()) return 0;

        double total = 0;

        for (Grade g : grades) {
            total += g.getScore();
        }

        return total / grades.size();
    }

    public void setName(String fullName) {
        String[] parts = fullName.trim().split(" ");

        if (parts.length >= 2) {
            this.firstName = parts[0];
            this.lastName = parts[1];
        } else {
            this.firstName = fullName;
            this.lastName = "";
        }
    }

    public void setParticipation(double score) {
        grades.removeIf(g -> g.getType().equals("Participation"));
        grades.add(new Grade("Participation", score));
    }
}