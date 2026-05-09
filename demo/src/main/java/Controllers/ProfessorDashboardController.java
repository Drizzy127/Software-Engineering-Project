package Controllers;

import Models.Course;
import Services.AppState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ProfessorDashboardController {

    @FXML private VBox courseContainer;
    @FXML private Label semesterLabel;
    @FXML private Label totalCoursesLabel;
    @FXML private Label emailLabel;

    private final String[] cardColors = {"#7ed6d4", "#e68484", "#d67adf", "#b7e07c", "#f4c177", "#9bc2e6"};

    @FXML
    public void initialize() {
        semesterLabel.setText("Fall 2026");
        emailLabel.setText(AppState.getLoggedInEmail());
        refreshCourses();
    }

    private void refreshCourses() {
        courseContainer.getChildren().clear();

        int index = 0;
        for (Course course : AppState.getCourses()) {
           //updates clas avg
            course.updateClassAverage();

            addCourseCard(course, cardColors[index % cardColors.length]);
            index++;
        }

        totalCoursesLabel.setText(String.valueOf(AppState.getCourses().size()));
    }

    private void addCourseCard(Course course, String bgColor) {
        HBox card = new HBox();
        card.setPrefWidth(520);
        card.setMinHeight(170);
        card.setPadding(new Insets(18));
        card.setSpacing(20);
        card.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-color: #444444;"
        );

        VBox leftSide = new VBox();
        leftSide.setSpacing(6);
        HBox.setHgrow(leftSide, Priority.ALWAYS);

        Label titleLabel = new Label(course.getCourseCode() + " - " + course.getCourseName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label semesterSectionLabel = new Label(course.getSemester() + " | " + course.getSection());
        semesterSectionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label studentsLabel = new Label("Students: " + course.getStudentCount());
        studentsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        Label timeLabel = new Label(course.getMeetingTime());
        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        Label roomLabel = new Label("Room : " + course.getRoom());
        roomLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label avgLabel = new Label("Class Avg: " + course.getClassAverage());
        avgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        leftSide.getChildren().addAll(titleLabel, semesterSectionLabel, studentsLabel, timeLabel, roomLabel, spacer, avgLabel);

        VBox rightSide = new VBox();
        rightSide.setSpacing(10);
        rightSide.setPrefWidth(120);

        Button viewButton = new Button("View\nCourse");
        viewButton.setWrapText(true);
        viewButton.setTextFill(Color.WHITE);
        viewButton.setPrefSize(120, 80);
        viewButton.setStyle("-fx-background-color: #6f63ff; -fx-font-size: 16px; -fx-font-family: 'Serif';");
        viewButton.setOnAction(e -> openCourseView(course));

        Button removeButton = new Button("Remove");
        removeButton.setPrefSize(120, 35);
        removeButton.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px;");
        removeButton.setOnAction(e -> {
            AppState.removeCourse(course);
            refreshCourses();
        });

        rightSide.getChildren().addAll(viewButton, removeButton);
        card.getChildren().addAll(leftSide, rightSide);
        courseContainer.getChildren().add(card);
    }



    private void openCourseView(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/CourseViewPage.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);

            CourseViewController controller = loader.getController();
            controller.setCourse(course);

            controller.setOnCourseUpdate(() -> {
                refreshCourses();
            });


            Stage stage = (Stage) courseContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void forceRefreshCourses() {
        courseContainer.getChildren().clear();
        refreshCourses();
    }

    @FXML
    private void goToAddCourse(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/AddCoursePage.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goToSocial(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/SocialTabPage.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
