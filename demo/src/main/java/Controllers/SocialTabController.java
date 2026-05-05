package Controllers;

import Services.AppState;
import Services.SocialPost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SocialTabController {

    @FXML private TextArea postInput;
    @FXML private VBox feedContainer;

    @FXML
    public void initialize() {
        refreshFeed();
    }

    private void refreshFeed() {
        feedContainer.getChildren().clear();
        for (SocialPost post : AppState.getPosts()) {
            addPostCard(post.getAuthor(), post.getDepartment(), post.getContent(), post.getTime());
        }
    }

    @FXML
    private void handlePost() {
        String content = postInput.getText().trim();

        if (!content.isEmpty()) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("h:mma")).toLowerCase();
            AppState.addPost(new SocialPost(AppState.getLoggedInEmail(), "Comp Sys", content, time));
            postInput.clear();
            refreshFeed();
        }
    }

    private void addPostCard(String author, String department, String content, String time) {
        VBox postCard = new VBox();
        postCard.setSpacing(10);
        postCard.setStyle(
                "-fx-background-color: #d7e4d1;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 0 0 2 0;" +
                        "-fx-padding: 16;"
        );

        HBox headerRow = new HBox();
        headerRow.setSpacing(10);

        Label authorLabel = new Label(author);
        authorLabel.setWrapText(true);
        authorLabel.setMaxWidth(360);
        authorLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #213321;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label deptLabel = new Label(department);
        deptLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #213321;");

        headerRow.getChildren().addAll(authorLabel, spacer, deptLabel);

        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(560);
        contentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #213321;");

        HBox bottomRow = new HBox();
        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        Label timeLabel = new Label("(Posted " + time + ")");
        timeLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #213321;");

        bottomRow.getChildren().addAll(bottomSpacer, timeLabel);
        postCard.getChildren().addAll(headerRow, contentLabel, bottomRow);
        feedContainer.getChildren().add(postCard);
    }

    @FXML
    private void goToDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/ProfessorDashboardPage.fxml"));
        Scene scene = new Scene(loader.load(), 1200, 800);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
