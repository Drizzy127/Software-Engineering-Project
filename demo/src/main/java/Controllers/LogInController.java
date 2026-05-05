package Controllers;

import Services.AppState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Temporary local login for the GUI demo.
        // This lets the dashboard and social tab work without Firebase/Firestore.
        if (!username.isEmpty() && (password.equals("1234") || !password.isEmpty())) {
            try {
                AppState.setLoggedInEmail(username);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/ProfessorDashboardPage.fxml"));
                Scene scene = new Scene(loader.load(), 1200, 800);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Professor Dashboard");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Enter an email and password");
        }
    }
}
