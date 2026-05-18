package Controllers;

import Models.Course;
import Models.Student;
import Models.Grade;
import Services.AppState;
import Services.FirestoreService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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
        lastNameField.setText(parts.length > 1 ? parts[1] : "");
        refreshGrades();
    }

    public void setCourse(Course course) {
        this.course = course;
        refreshGrades();
    }

    public void setOnSave(Runnable onSave) {
        this.onSave = onSave;
    }

    @FXML
    private void saveAll() {
        student.setName(firstNameField.getText().trim() + " " + lastNameField.getText().trim());
        if (course != null) course.updateClassAverage();
        saveCourse();
        if (onSave != null) onSave.run();
        goBackToCourse();
    }

    @FXML
    private void addGrade() {
        Dialog<Grade> dialog = new Dialog<>();
        dialog.setTitle("Add Grade");
        dialog.setHeaderText("Add assignment, quiz, test, or participation grade");

        ButtonType addButton = new ButtonType("Add Grade", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Quiz", "Assignment", "Test", "Participation");
        typeBox.setValue("Assignment");

        TextField nameField = new TextField();
        nameField.setPromptText("Example: Homework 1 or Midterm");
        TextField scoreField = new TextField();
        scoreField.setPromptText("Score, example: 95");
        TextArea notesArea = new TextArea();
        notesArea.setPromptText("Optional notes");
        notesArea.setPrefRowCount(3);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Type"), 0, 0);
        grid.add(typeBox, 1, 0);
        grid.add(new Label("Name"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Score"), 0, 2);
        grid.add(scoreField, 1, 2);
        grid.add(new Label("Notes"), 0, 3);
        grid.add(notesArea, 1, 3);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button == addButton) {
                try {
                    return new Grade(typeBox.getValue(), Double.parseDouble(scoreField.getText().trim()), nameField.getText().trim(), notesArea.getText().trim());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(grade -> {
            if (grade.getType().equals("Participation")) {
                student.setParticipation(grade.getScore(), grade.getName(), grade.getNotes());
            } else {
                student.getGrades().add(grade);
            }
            if (course != null) course.updateClassAverage();
            saveCourse();
            refreshGrades();
        });
    }

    private void refreshGrades() {
        if (gradeContainer == null || student == null) return;
        gradeContainer.getChildren().clear();

        for (Grade g : student.getGrades()) {
            VBox card = new VBox(4);
            card.setStyle("-fx-padding: 12; -fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #d9ead3; -fx-border-radius: 10;");

            String title = g.getType();
            if (g.getName() != null && !g.getName().isBlank()) title += " - " + g.getName();

            Label titleLabel = new Label(title);
            titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1b5e20;");
            Label scoreLabel = new Label("Score: " + g.getScore());
            scoreLabel.setStyle("-fx-text-fill: #2e7d32;");

            card.getChildren().addAll(titleLabel, scoreLabel);
            if (g.getNotes() != null && !g.getNotes().isBlank()) {
                Label notes = new Label("Notes: " + g.getNotes());
                notes.setWrapText(true);
                notes.setStyle("-fx-text-fill: #555555;");
                card.getChildren().add(notes);
            }
            gradeContainer.getChildren().add(card);
        }

        if (course != null) {
            averageLabel.setText("Weighted Average: " + Course.toLetterGrade(course.calculateStudentWeightedAverage(student)));
        } else {
            averageLabel.setText("Average: " + String.format("%.2f", student.getRawAverage()));
        }
    }

    private void saveCourse() {
        try {
            if (course != null && AppState.getLoggedInUid() != null) {
                FirestoreService firestore = new FirestoreService();
                firestore.saveCourse(AppState.getLoggedInUid(), course);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Could not save student changes: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void goBackToCourse() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/Pages/CourseViewPage.fxml"));
            javafx.scene.Parent root = loader.load();
            CourseViewController controller = loader.getController();
            controller.setCourse(course);
            javafx.stage.Stage stage = (javafx.stage.Stage) firstNameField.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root, 1200, 800));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
