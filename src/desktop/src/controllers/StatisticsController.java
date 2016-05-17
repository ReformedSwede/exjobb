package controllers;

import grammar.Word;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.Model;
import main.ResourceManager;
import main.Stats;

import java.io.IOException;
import java.net.URL;

public class StatisticsController {

    private Model model;
    private Stats stats;

    //Components
    private Pane pContent;
    @FXML
    private Label sessionTitle;
    @FXML
    private Label totNr;
    @FXML
    private VBox words;
    @FXML
    private VBox partsOfSpeech;
    @FXML
    private VBox inflections;


    public void init(Model model, Pane pane){
        pContent = pane;
        this.model = model;
        stats = model.getStats();

        //Init session title
        sessionTitle.setText("Native language: " + ResourceManager.codeToName(model.getNativeLangCode()) +
                ", foreign language: " + ResourceManager.codeToName(model.getForeignLangCode()));

        //Display statistics
        totNr.setText("Correct answers: " + stats.getNrOfRights() + " / " +
                (stats.getNrOfRights() + stats.getNrOfWrongs()));

        for(String word : stats.getStatsForWords().keySet())
            words.getChildren().add(new Label(word + ": " + stats.getStatsForWords().get(word) + "%"));

        for(String inflection : stats.getStatsForInflections().keySet())
            inflections.getChildren().add(new Label(inflection + ": " + stats.getStatsForInflections().get(inflection) + "%"));

        for(String pos : stats.getStatsForPartsOfSpeech().keySet())
            partsOfSpeech.getChildren().add(new Label(pos + ": " + stats.getStatsForPartsOfSpeech().get(pos) + "%"));
    }


    public void back(){
        //Load FXML
        URL url = getClass().getResource("/resources/view/practice-window.fxml");
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(url);
        fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());

        pContent.getChildren().clear();
        try {
            pContent.getChildren().add(fxmlloader.load(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Init edit controller
        ((PracticeController)fxmlloader.getController()).init(model, pContent);
    }

    public void exit(){
        model.endSession();

        //Load FXML
        URL url = getClass().getResource("/resources/view/start-window.fxml");
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(url);
        fxmlloader.setBuilderFactory(new JavaFXBuilderFactory());

        pContent.getChildren().clear();
        try {
            pContent.getChildren().add(fxmlloader.load(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((StartController)fxmlloader.getController()).initialize(null, null);
    }
}
