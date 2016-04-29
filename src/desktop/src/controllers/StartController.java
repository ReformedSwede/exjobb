package controllers;

import grammar.GrammarManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;
import main.Utils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Pair;
import main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StartController implements Initializable {

    private Model model = new Model();

    //UI components
    public Pane pContent;
    public GridPane sessionsList;
    //public VBox buttonGrid;
    public ComboBox<String> nativeBox;
    public ComboBox<String> foreignBox;
    public Button addNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshSessionsList();

        ObservableList<String> availableLanguages = FXCollections.observableArrayList();
        availableLanguages.addAll(Utils.getGfLanguages().stream().map(Utils::codeToName).collect(Collectors.toList()));
        nativeBox.setItems(availableLanguages);
        foreignBox.setItems(availableLanguages);
    }

    public void addNewSession(){
        String sessionNative = nativeBox.getSelectionModel().getSelectedItem();
        String sessionForeign = foreignBox.getSelectionModel().getSelectedItem();
        GrammarManager.createSession(sessionNative, sessionForeign);

        refreshSessionsList();
    }

    private void refreshSessionsList(){
        //Clear previous content
        sessionsList.getChildren().clear();

        List<Pair<String, String>> sessions = GrammarManager.getSessions();
        for(int row = 0; row < sessions.size(); row++){
            //Add labels
            HBox box = new HBox();
            GridPane.setConstraints(box, 0, row);
            sessionsList.getChildren().add(box);
            box.getChildren().add(new Label(Utils.codeToName(sessions.get(row).getKey())));
            box.getChildren().add(new Label(Utils.codeToName(sessions.get(row).getValue())));

            //Add buttons
            box = new HBox();
            GridPane.setConstraints(box, 1, row);
            sessionsList.getChildren().add(box);
            Pair<String, String> session = sessions.get(row);

            //Launch button
            Button b = new Button("Launch session");
            b.setOnAction(event -> StartController.this.startPractice(session.getKey(), session.getValue()));
            b.setId("launchBtn");
            box.getChildren().add(b);

            //Delete button
            b = new Button("Delete");
            b.setOnAction(event -> StartController.this.deleteSession(session.getKey(), session.getValue()));
            b.setId("removeBtn");
            box.getChildren().add(b);
        }
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

    private void deleteSession(String nativeLangCode, String foreignLangCode){
        GrammarManager.removeSession(nativeLangCode, foreignLangCode);
        refreshSessionsList();
    }
}
