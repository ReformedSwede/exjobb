package controllers;

import grammar.Word;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import main.Model;
import main.ResourceManager;
import main.Stats;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        List<String> list = new ArrayList<>(stats.getStatsForWords().keySet());
        list.sort((o1, o2) -> stats.getStatsForWords().get(o2).compareTo(stats.getStatsForWords().get(o1)));
        for(String word : list){
            Label label = new Label(word + ": " + stats.getStatsForWords().get(word) + "%");
            words.getChildren().add(label);
        }

        list = new ArrayList<>(stats.getStatsForInflections().keySet());
        list.sort((o1, o2) -> stats.getStatsForInflections().get(o2).compareTo(stats.getStatsForInflections().get(o1)));
        for(String inflection : list){
            Label label = new Label(inflection + ": " + stats.getStatsForInflections().get(inflection) + "%");
            inflections.getChildren().add(label);
        }

        list = new ArrayList<>(stats.getStatsForPartsOfSpeech().keySet());
        list.sort((o1, o2) -> stats.getStatsForPartsOfSpeech().get(o2).compareTo(stats.getStatsForPartsOfSpeech().get(o1)));
        for(String pos : list){
            Label label = new Label(pos + ": " + stats.getStatsForPartsOfSpeech().get(pos) + "%");
            partsOfSpeech.getChildren().add(label);
        }
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
