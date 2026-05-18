package Controllers;

import Services.AppState;
import Services.FirestoreService;
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
        try {
            FirestoreService firestore = new FirestoreService();
            AppState.setPosts(firestore.loadPosts());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            SocialPost post = new SocialPost(AppState.getLoggedInEmail(), AppState.getLoggedInDepartment(), content, time);
            try {
                FirestoreService firestore = new FirestoreService();
                firestore.savePost(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
            AppState.addPost(post);
            postInput.clear();
            refreshFeed();
        }
    }

    private void addPostCard(String author, String department, String content, String time) {
        VBox postCard = new VBox();
        postCard.setSpacing(10);
        postCard.setStyle(
                "-fx-background-color: white;" +
                        "-fx-padding: 16;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 8, 0.2, 0, 2);"
        );

        HBox headerRow = new HBox();
        headerRow.setSpacing(10);

        Label authorLabel = new Label(author);
        authorLabel.setWrapText(true);
        authorLabel.setMaxWidth(360);
        authorLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label deptLabel = new Label(department);
        deptLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        headerRow.getChildren().addAll(authorLabel, spacer, deptLabel);

        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(560);
        contentLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #333;");

        HBox bottomRow = new HBox();
        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        Label timeLabel = new Label("(Posted " + time + ")");
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

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
