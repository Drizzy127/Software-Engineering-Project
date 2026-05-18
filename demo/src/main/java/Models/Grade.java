package Models;

public class Grade {

    private String type;
    private double score;
    private String name;
    private String notes;

    public Grade(String type, double score) {
        this(type, score, "", "");
    }

    public Grade(String type, double score, String name, String notes) {
        this.type = type;
        this.score = score;
        this.name = name == null ? "" : name;
        this.notes = notes == null ? "" : notes;
    }

    public String getType() { return type; }
    public double getScore() { return score; }
    public String getName() { return name; }
    public String getNotes() { return notes; }

    public void setType(String type) { this.type = type; }
    public void setScore(double score) { this.score = score; }
    public void setName(String name) { this.name = name == null ? "" : name; }
    public void setNotes(String notes) { this.notes = notes == null ? "" : notes; }
}
