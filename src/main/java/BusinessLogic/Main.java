package BusinessLogic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Initializes and starts music player
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("MMP");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image("images/logo.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}