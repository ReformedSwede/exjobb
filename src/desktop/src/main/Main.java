package main;

import controllers.StartController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/start-window.fxml"));
        primaryStage.setTitle("Grammar");

        Scene scene = new Scene(root, 700, 400);
        scene.getStylesheets().add("/resources/css/style.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {launch(args);}
}
