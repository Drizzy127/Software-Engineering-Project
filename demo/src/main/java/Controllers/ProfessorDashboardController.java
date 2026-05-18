package Controllers;

import Models.Course;
import Services.AppState;
import Services.FirestoreService;
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

    private final String[] cardColors = {"white"};

    @FXML
    public void initialize() {
        try {
            if (AppState.getLoggedInUid() != null) {
                FirestoreService firestore = new FirestoreService();
                AppState.setCourses(firestore.loadCourses(AppState.getLoggedInUid()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 10, 0.2, 0, 3);"
        );

        VBox leftSide = new VBox();
        leftSide.setSpacing(6);
        HBox.setHgrow(leftSide, Priority.ALWAYS);

        Label titleLabel = new Label(course.getCourseCode() + " - " + course.getCourseName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label semesterSectionLabel = new Label(course.getSemester() + " | " + course.getSection());
        semesterSectionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label studentsLabel = new Label("Students: " + course.getStudentCount());
        studentsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Label timeLabel = new Label(course.getMeetingTime());
        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Label roomLabel = new Label("Room : " + course.getRoom());
        roomLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label avgLabel = new Label("Class Avg: " + course.getClassAverageText());
        avgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");

        leftSide.getChildren().addAll(titleLabel, semesterSectionLabel, studentsLabel, timeLabel, roomLabel, spacer, avgLabel);

        VBox rightSide = new VBox();
        rightSide.setSpacing(10);
        rightSide.setPrefWidth(120);

        Button viewButton = new Button("View\nCourse");
        viewButton.setWrapText(true);
        viewButton.setTextFill(Color.WHITE);
        viewButton.setPrefSize(120, 80);
        viewButton.setStyle("-fx-background-color: #2e7d32; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 8;");
        viewButton.setOnAction(e -> openCourseView(course));

        Button removeButton = new Button("Remove");
        removeButton.setPrefSize(120, 35);
        removeButton.setStyle("-fx-background-color: #1b5e20; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8;");
        removeButton.setOnAction(e -> {
            try {
                FirestoreService firestore = new FirestoreService();
                firestore.deleteCourse(AppState.getLoggedInUid(), course);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
    private void handleLogout(ActionEvent event) throws Exception {
        AppState.setLoggedInUid(null);
        AppState.setLoggedInEmail("");
        AppState.setLoggedInDepartment("General");
        AppState.setCourses(new java.util.ArrayList<>());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/LogInPage.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
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
