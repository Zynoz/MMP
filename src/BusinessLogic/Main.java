package BusinessLogic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/views/sample.fxml"));
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
        primaryStage.getIcons().add(new Image("resources/logo.png"));

//ToDo not working as intended
//        TestController testController = fxmlLoader.getController();
//        scene.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.RIGHT) {
//                testController.nextSong();
//            }
//            if (event.getCode() == KeyCode.SPACE) {
//                testController.playPause();
//            }
//        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}