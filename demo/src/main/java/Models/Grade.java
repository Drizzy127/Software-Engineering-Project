package Models;

public class Grade {

    private String type;
    private double score;

    public Grade(String type, double score) {
        this.type = type;
        this.score = score;
    }

    public String getType() {
        return type;
    }

    public double getScore() {
        return score;
    }
}