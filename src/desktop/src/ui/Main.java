package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.grammaticalframework.pgf.PGF;

import java.io.FileNotFoundException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");

        Scene scene = new Scene(root, 1000, 800);
        scene.getStylesheets().add("/src/resources/style.css");

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        try {
            PGF pgf = PGF.readPGF("Words.pgf");
            pgf.getCategories();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //launch(args);
    }
}
