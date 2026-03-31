package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProfessorDashboardController
{
    //WHAT I PUT IN THE CLASS IS JUST A PLACEHOLDER/TEMPLATE FOR THE CONTROLLERS, FEEL FREE TO CHANGE COMPLETELY
    // UI elements from FXML
    @FXML
    private Button myButton;

    @FXML
    private TextField myTextField;

    // Runs automatically when FXML loads
    @FXML
    public void initialize() {
        // setup code here
    }

    // Event handler (button click, etc.)
    @FXML
    private void handleButtonClick() {
        System.out.println("Clicked: " + myTextField.getText());
    }
}
