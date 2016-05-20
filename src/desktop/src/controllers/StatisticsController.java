package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import main.Model;
import main.ResourceManager;
import main.Stats;
import main.SyncThread;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsController {

    private Model model;
    private Stats stats;
    private final int goodLimit = 90;
    private final int badLimit = 30;
    private final String goodColor = "#00aa00";
    private final String badColor = "#cc0000";

    //Components
    private Pane pContent;
    @FXML
    private Label sessionTitle;
    @FXML
    private Label totNr;
    @FXML
    private ListView<Label> words;
    @FXML
    private ListView<Label> partsOfSpeech;
    @FXML
    private ListView<Label> inflections;

    /**
     * Initialize window and display statistics
     * @param model The model
     * @param pane The view
     */
    void init(Model model, Pane pane){
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
        words.setItems(FXCollections.observableList(list.stream().map(word -> {
            int percent = stats.getStatsForWords().get(word);
            String color = percent > goodLimit ? goodColor : percent < badLimit ? badColor : "#000000";
            Label label = new Label(word + ": " + stats.getStatsForWords().get(word) + "%");
            label.setStyle("-fx-text-fill:" + color + ";");
            return label;
        }).collect(Collectors.toList())));

        list = new ArrayList<>(stats.getStatsForPartsOfSpeech().keySet());
        list.sort((o1, o2) -> stats.getStatsForPartsOfSpeech().get(o2).compareTo(stats.getStatsForPartsOfSpeech().get(o1)));
        partsOfSpeech.setItems(FXCollections.observableList(list.stream().map(partOfSpeech -> {
            int percent = stats.getStatsForPartsOfSpeech().get(partOfSpeech);
            String color = percent > goodLimit ? goodColor : percent < badLimit ? badColor : "#000000";
            Label label = new Label(partOfSpeech + ": " + stats.getStatsForPartsOfSpeech().get(partOfSpeech) + "%");
            label.setStyle("-fx-text-fill:" + color + ";");
            return label;
        }).collect(Collectors.toList())));

        list = new ArrayList<>(stats.getStatsForInflections().keySet());
        list.sort((o1, o2) -> stats.getStatsForInflections().get(o2).compareTo(stats.getStatsForInflections().get(o1)));
        inflections.setItems(FXCollections.observableList(list.stream().map(inflection -> {
            int percent = stats.getStatsForInflections().get(inflection);
            String color = percent > goodLimit ? goodColor : percent < badLimit ? badColor : "#000000";
            Label label = new Label(inflection + ": " + stats.getStatsForInflections().get(inflection) + "%");
            label.setStyle("-fx-text-fill:" + color + ";");
            return label;
        }).collect(Collectors.toList())));
    }

    /**
     * Clear all statistical data and refresh view
     */
    public void resetStats(){
        model.resetStats();
        init(model, pContent);
    }

    /**
     * Return to practice window
     */
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

    /**
     * Exit session to start window
     */
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
