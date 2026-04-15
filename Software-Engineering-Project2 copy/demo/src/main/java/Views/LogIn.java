package Views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

//launcher so that it opens Login.fxml first
public class LogIn extends Application
{
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/Pages/LogInPage.fxml") // your FXML
        );
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

    //user authentication
        //make it so each account is apart of a certain department,
            //unique id for each department ex: english, comp sci
    //Professor Login