package Controllers;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class ProfessorDashboardController {

    @FXML
    private VBox courseContainer;

    @FXML
    private Label semesterLabel;

    @FXML
    private Label totalCoursesLabel;

    @FXML
    private void goToSocial(javafx.event.ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/SocialTabPage.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void initialize() {
        semesterLabel.setText("Fall 2026");

        addCourseCard(
                "CSC 101 - Intro to CS",
                "Fall 2026",
                "Section 01",
                35,
                "Mon/Wed 10:00AM",
                "Whitman 204",
                "C",
                "#7ed6d4"
        );

        addCourseCard(
                "CSC 171 - Database Management",
                "Fall 2026",
                "Section 032",
                25,
                "Mon/Wed 12:15PM",
                "Whitman 220",
                "A+",
                "#e68484"
        );

        addCourseCard(
                "CSC 101 - Intro to CS",
                "Fall 2026",
                "Section 01",
                35,
                "Mon/Wed 10:00AM",
                "Whitman 204",
                "C",
                "#d67adf"
        );

        totalCoursesLabel.setText(String.valueOf(courseContainer.getChildren().size()));
    }

    private void addCourseCard(String courseName,
                               String semester,
                               String section,
                               int studentCount,
                               String meetingTime,
                               String room,
                               String classAverage,
                               String bgColor) {

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

        Label titleLabel = new Label(courseName);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label semesterSectionLabel = new Label(semester + " | " + section);
        semesterSectionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label studentsLabel = new Label("Students: " + studentCount);
        studentsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        Label timeLabel = new Label(meetingTime);
        timeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        Label roomLabel = new Label("Room : " + room);
        roomLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label avgLabel = new Label("Class Avg-" + classAverage);
        avgLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        leftSide.getChildren().addAll(
                titleLabel,
                semesterSectionLabel,
                studentsLabel,
                timeLabel,
                roomLabel,
                spacer,
                avgLabel
        );

        StackPane buttonPane = new StackPane();
        buttonPane.setPrefSize(120, 110);
        buttonPane.setMaxSize(120, 110);
        buttonPane.setStyle("-fx-background-color: #6f63ff;");

        Button viewButton = new Button("View\nCourse");
        viewButton.setWrapText(true);
        viewButton.setTextFill(Color.WHITE);
        viewButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-family: 'Serif';"
        );

        viewButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/CourseDetailPage.fxml"));
                Scene scene = new Scene(loader.load(), 1200, 800);

                CourseDetailController controller = loader.getController();
                String code = courseName.contains(" - ") ? courseName.substring(0, courseName.indexOf(" - ")) : courseName;
                String name = courseName.contains(" - ") ? courseName.substring(courseName.indexOf(" - ") + 3) : courseName;
                controller.setCourseData(
                        code,
                        name,
                        semester,
                        section,
                        meetingTime,
                        room,
                        getCourseDescription(code, name)
                );

                Stage stage = (Stage) viewButton.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle(courseName);
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttonPane.getChildren().add(viewButton);

        card.getChildren().addAll(leftSide, buttonPane);
        courseContainer.getChildren().add(card);
    }

    private String getCourseDescription(String code, String name) {
        if (code.equals("CSC 101")) {
            return "Computers have become a part of everyday life across many academic disciplines. In this course, students acquire broad knowledge of computer science and information technology concepts, computational thinking, and using computers to solve real-world problems.";
        }
        if (code.equals("CSC 171")) {
            return "This course introduces database design, relational models, SQL, normalization, and how data is stored, queried, and managed in modern information systems.";
        }
        return "This course provides students with important concepts, assignments, meeting information, and roster access for the selected semester section.";
    }
}
