package Controllers;

import Services.AuthResult;
import Services.FirebaseAuth;
//import Services.FirestoreService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegisterController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> departmentBox;
    @FXML private Label messageLabel;

    private final FirebaseAuth authService = new FirebaseAuth();

    @FXML
    public void initialize() {
        departmentBox.getItems().addAll(
                "Arts & Sciences",
                "Business",
                "Engineering Technology",
                "Health Sciences"
        );
    }

    @FXML
    private void handleRegister() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String departmentName = departmentBox.getValue();

        if (email.isEmpty() || password.isEmpty() || departmentName == null) {
            messageLabel.setText("Email, password, and department are required.");
            return;
        }

        try {
            AuthResult result = authService.register(email, password);

            if (!result.isSuccess()) {
                messageLabel.setText("Registration failed: " + result.getRawResponse());
                return;
            }

            String departmentId = switch (departmentName) {
                case "Computer Science" -> "CS";
                case "English" -> "ENG";
                case "Math" -> "MATH";
                default -> "GEN";
            };

            //FirestoreService firestoreService = new FirestoreService();
            //firestoreService.saveUserDepartment(
                    //result.getUid(),
                    //result.getEmail(),
                    //departmentId,
                    //departmentName
            //);

            messageLabel.setText("Registered successfully.");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/Pages/LogInPage.fxml")
            );
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Login");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error: " + e.getMessage());
        }
    }
}