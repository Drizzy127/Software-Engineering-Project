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
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> departmentBox;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        departmentBox.getItems().addAll(
                "Computer & Information Systems",
                "Arts & Sciences",
                "Business",
                "Engineering Technology",
                "Health Sciences"
        );
        departmentBox.setValue("Computer & Information Systems");
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String departmentName = departmentBox.getValue();

        if (email.isEmpty() || password.isEmpty() || departmentName == null) {
            messageLabel.setText("Email, password, and department are required.");
            return;
        }

        try {
            FirebaseAuth auth = new FirebaseAuth();
            AuthResult result = auth.register(email, password, departmentName);

            FirestoreService firestore = new FirestoreService();
            AppState.setLoggedInUid(result.getUid());
            AppState.setLoggedInEmail(result.getEmail());
            AppState.setLoggedInDepartment(departmentName);
            AppState.setCourses(firestore.loadCourses(result.getUid()));

            goToDashboard(event);
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Firebase registration error: " + e.getMessage());
        }
    }

    @FXML
    private void goToLogin(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pages/LogInPage.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
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
