package Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;






public class ProfessorDashboard extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/Pages/ProfessorDashboardPage.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Professor Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

