package controllers;

import grammar.GrammarManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import main.Utils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Pair;
import main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StartController implements Initializable {

    private Model model = new Model();

    //UI components
    public Pane pContent;
    public TilePane sessionsGrid;
    public ComboBox<String> nativeBox;
    public ComboBox<String> foreignBox;
    public Button addNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sessionsGrid.getChildren().clear();
        List<Pair<String, String>> sessions = GrammarManager.getSessions();

        sessions.forEach(session -> {
            HBox box = new HBox();
            box.getChildren().add(new Label(Utils.codeToName(session.getKey())));
            box.getChildren().add(new Label(Utils.codeToName(session.getValue())));
            Button b = new Button("Launch session");
            b.setOnAction(event -> startPractice(session.getKey(), session.getValue()));
            box.getChildren().add(b);
            sessionsGrid.getChildren().add(box);
        });

        ObservableList<String> availableLanguages = FXCollections.observableArrayList();
        availableLanguages.addAll(Utils.getGfLanguages().stream().map(Utils::codeToName).collect(Collectors.toList()));
        nativeBox.setItems(availableLanguages);
        foreignBox.setItems(availableLanguages);
    }

    public void addNewSession(){
        String sessionNative = nativeBox.getSelectionModel().getSelectedItem();
        String sessionForeign = foreignBox.getSelectionModel().getSelectedItem();
        GrammarManager.createSession(sessionNative, sessionForeign);

        //Refresh list
        sessionsGrid.getChildren().clear();
        List<Pair<String, String>> sessions = GrammarManager.getSessions();

        sessions.forEach(session -> {
            HBox box = new HBox();
            box.getChildren().add(new Label(Utils.codeToName(session.getKey())));
            box.getChildren().add(new Label(Utils.codeToName(session.getValue())));
            Button b = new Button("Launch session");
            b.setOnAction(event -> startPractice(session.getKey(), session.getValue()));
            box.getChildren().add(b);
            sessionsGrid.getChildren().add(box);
        });
    }

    private void startPractice(String nativeLang, String foreignLang){
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

        model.initialize(nativeLang, foreignLang);
        ((PracticeController)fxmlloader.getController()).init(model, pContent);
    }
}
