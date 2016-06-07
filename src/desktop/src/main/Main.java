package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

/**
 * Main class. Used for initializing and starting application
 */
public class Main extends Application {

    /**
     * Initialized the stage and scene
     * @param primaryStage The primary stage of the application
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/start-window.fxml"));
        primaryStage.setTitle("Grammar");

        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add("/resources/css/window-style.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Starts the application
     * @param args Command line arguments
     */
    public static void main(String[] args) {launch(args);}
}
