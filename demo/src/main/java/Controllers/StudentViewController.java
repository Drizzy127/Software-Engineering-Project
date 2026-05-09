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
    private Runnable onSave;

    public void setStudent(Student student) {
        this.student = student;

        String[] parts = student.getName().split(" ", 2);

        firstNameField.setText(parts[0]);

        if (parts.length > 1) {
            lastNameField.setText(parts[1]);
        } else {
            lastNameField.setText("");
        }

        refreshGrades();
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void saveAll() {

        String fullName =
                firstNameField.getText().trim() + " " +
                        lastNameField.getText().trim();

        student.setName(fullName);

        if (course != null) {
            course.updateClassAverage();
        }

        refreshGrades();

        if (onSave != null) {
            onSave.run();
        }

        try {

            javafx.fxml.FXMLLoader loader =
                    new javafx.fxml.FXMLLoader(
                            getClass().getResource("/Pages/CourseViewPage.fxml")
                    );

            javafx.scene.Parent root = loader.load();

            CourseViewController controller = loader.getController();

            controller.setCourse(course);

            javafx.stage.Stage stage =
                    (javafx.stage.Stage) firstNameField.getScene().getWindow();

            stage.setScene(new javafx.scene.Scene(root, 1200, 800));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Changes saved");
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