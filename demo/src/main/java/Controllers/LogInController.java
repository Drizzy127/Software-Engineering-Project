package Controllers;

import Services.AppState;
import Services.AuthResult;
import Services.FirebaseAuth;
import Services.FirestoreService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Email and password are required.");
            return;
        }

        try {
            FirebaseAuth auth = new FirebaseAuth();
            AuthResult result = auth.login(username, password);

            if (result.isSuccess()) {
                FirestoreService firestore = new FirestoreService();
                AppState.setLoggedInUid(result.getUid());
                AppState.setLoggedInEmail(result.getEmail());
                AppState.setLoggedInDepartment(firestore.getDepartmentName(result.getUid()));
                AppState.setCourses(firestore.loadCourses(result.getUid()));
                goToDashboard(event);
            } else {
                messageLabel.setText("Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Firebase login error: " + e.getMessage());
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/RegisterPage.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }

    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/ProfessorDashboardPage.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Professor Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Could not open dashboard.");
        }
    }
}
