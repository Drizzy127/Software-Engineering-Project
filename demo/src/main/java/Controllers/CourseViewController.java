package Controllers;

import Models.Course;
import Models.Student;
import Services.AppState;
import Services.FirestoreService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CourseViewController {

    @FXML private Label courseNameLabel;
    @FXML private Label semesterLabel;
    @FXML private Label sectionLabel;
    @FXML private Label studentsLabel;
    @FXML private Label timeLabel;
    @FXML private Label roomLabel;
    @FXML private Label avgLabel;
    @FXML private VBox courseContainer;

    @FXML private TextField quizWeightField;
    @FXML private TextField testWeightField;
    @FXML private TextField assignmentWeightField;
    @FXML private TextField participationWeightField;

    private Course course;
    private Runnable onCourseUpdate;

    public void setCourse(Course course) {
        this.course = course;
        if (course == null) return;

        courseNameLabel.setText(course.getCourseName());
        semesterLabel.setText(course.getSemester());
        sectionLabel.setText(course.getSection());
        timeLabel.setText(course.getMeetingTime());
        roomLabel.setText(course.getRoom());

        quizWeightField.setText(String.valueOf(course.getQuizWeight()));
        testWeightField.setText(String.valueOf(course.getTestWeight()));
        assignmentWeightField.setText(String.valueOf(course.getAssignmentWeight()));
        participationWeightField.setText(String.valueOf(course.getParticipationWeight()));

        refreshCourseView();
    }

    @FXML
    private void applyWeights() {
        try {
            double quiz = parseWeight(quizWeightField.getText());
            double test = parseWeight(testWeightField.getText());
            double assignment = parseWeight(assignmentWeightField.getText());
            double participation = parseWeight(participationWeightField.getText());

            double total = quiz + test + assignment + participation;
            if (Math.abs(total - 1.0) > 0.01) {
                showAlert("Weights must add up to 1.00, or 100%.");
                return;
            }

            course.setQuizWeight(quiz);
            course.setTestWeight(test);
            course.setAssignmentWeight(assignment);
            course.setParticipationWeight(participation);
            saveCourse();
            refreshCourseView();
        } catch (NumberFormatException e) {
            showAlert("Enter weights as decimals like 0.20 or percentages like 20.");
        }
    }

    private double parseWeight(String text) {
        double value = Double.parseDouble(text.trim());
        return value > 1 ? value / 100.0 : value;
    }

    @FXML
    private void addStudent() {
        Dialog<Student> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Enter the student's name");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        TextField firstName = new TextField();
        firstName.setPromptText("First name");
        TextField lastName = new TextField();
        lastName.setPromptText("Last name");
        VBox content = new VBox(10, new Label("First Name"), firstName, new Label("Last Name"), lastName);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == addButton && !firstName.getText().trim().isEmpty()) {
                return new Student(firstName.getText().trim(), lastName.getText().trim());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(student -> {
            course.addStudent(student);
            saveCourse();
            refreshCourseView();
        });
    }

    private void showRoster(Course course) {
        courseContainer.getChildren().clear();

        for (Student student : course.getStudents()) {
            HBox row = new HBox(15);
            row.setStyle("-fx-padding: 12; -fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-color: #d9ead3; -fx-border-radius: 10;");
            row.setPickOnBounds(true);

            Label nameLabel = new Label(student.getFirstName() + " " + student.getLastName());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1b5e20;");

            Label avg = new Label("Weighted Avg: " + Course.toLetterGrade(course.calculateStudentWeightedAverage(student)));
            avg.setStyle("-fx-text-fill: #2e7d32;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            Button dropButton = new Button("Drop");
            dropButton.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #b71c1c; -fx-border-color: #b71c1c; -fx-border-radius: 8; -fx-background-radius: 8;");
            dropButton.setOnAction(e -> dropStudent(student));

            row.setOnMouseClicked(e -> openStudentView(student));
            nameLabel.setOnMouseClicked(e -> openStudentView(student));
            avg.setOnMouseClicked(e -> openStudentView(student));

            row.getChildren().addAll(nameLabel, avg, spacer, dropButton);
            courseContainer.getChildren().add(row);
        }
    }

    private void dropStudent(Student student) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Drop Student");
        confirm.setHeaderText("Drop " + student.getName() + " from this course?");
        confirm.setContentText("Their saved grades for this course will also be removed.");
        confirm.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                course.removeStudent(student);
                saveCourse();
                refreshCourseView();
            }
        });
    }

    private void openStudentView(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/StudentViewPage.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            StudentViewController controller = loader.getController();
            controller.setStudent(student);
            controller.setCourse(course);
            controller.setOnSave(() -> { saveCourse(); refreshCourseView(); });
            Stage stage = (Stage) courseNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Student Profile");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshCourseView() {
        course.updateClassAverage();
        avgLabel.setText("Class Avg: " + course.getClassAverageText());
        studentsLabel.setText("Students: " + course.getStudentCount());
        showRoster(course);
        if (onCourseUpdate != null) onCourseUpdate.run();
    }

    private void saveCourse() {
        try {
            if (AppState.getLoggedInUid() != null) {
                FirestoreService firestore = new FirestoreService();
                firestore.saveCourse(AppState.getLoggedInUid(), course);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Could not save course changes: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBackMouse(ActionEvent event) throws Exception {
        saveCourse();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/ProfessorDashboardPage.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setOnCourseUpdate(Runnable onCourseUpdate) {
        this.onCourseUpdate = onCourseUpdate;
    }
}
