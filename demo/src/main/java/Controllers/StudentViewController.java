package Controllers;

import Models.Course;
import Models.Student;
import Models.Grade;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class StudentViewController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;

    @FXML private VBox gradeContainer;
    @FXML private Label averageLabel;

    private Student student;
    private Course course;

    public void setStudent(Student student) {
        this.student = student;

        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());

        refreshGrades();
    }

    @FXML
    private void saveAll() {
        System.out.println("Save All clicked");
    }

    @FXML
    private void saveName() {
        student.setName(
                firstNameField.getText().trim() + " " +
                        lastNameField.getText().trim()
        );
    }

    @FXML
    private void addGrade() {

        ChoiceDialog<String> typeDialog =
                new ChoiceDialog<>("Quiz", "Quiz", "Assignment", "Test", "Participation");

        typeDialog.setHeaderText("Select grade type");

        typeDialog.showAndWait().ifPresent(type -> {

            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Enter " + type + " score");

            inputDialog.showAndWait().ifPresent(input -> {
                try {
                    double score = Double.parseDouble(input);

                    if (type.equals("Participation")) {
                        student.setParticipation(score);
                    } else {
                        student.addGrade(type, score);
                    }

                    refreshGrades();

                } catch (NumberFormatException e) {
                    System.out.println("Invalid score");
                }
            });
        });
    }

    private void refreshGrades() {

        gradeContainer.getChildren().clear();

        for (Grade g : student.getGrades()) {

            Label label = new Label(
                    g.getType() + ": " + g.getScore()
            );

            gradeContainer.getChildren().add(label);
        }

        averageLabel.setText(
                "Average: " + String.format("%.2f", student.getRawAverage())
        );
    }
}