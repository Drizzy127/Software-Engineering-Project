package Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfessorDashboard {

    private ProfessorDashboard() {
        //utility class
    }

    public static void show(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                ProfessorDashboard.class.getResource("/Pages/ProfessorDashboardPage.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Professor Dashboard");
        stage.setScene(scene);
        stage.show();
    }
}
