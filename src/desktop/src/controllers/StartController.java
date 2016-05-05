package controllers;

import grammar.GrammarManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import main.ResourceManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.Model;
import main.Session;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StartController implements Initializable {

    private Model model = new Model();

    //UI components
    public Pane pContent;
    public GridPane sessionsList;
    //public VBox buttonGrid;
    public ComboBox<String> nativeBox;
    public ComboBox<String> foreignBox;
    public TextField titleField;
    public Button addNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshSessionsList();

        ObservableList<String> availableLanguages = FXCollections.observableArrayList();
        availableLanguages.addAll(ResourceManager.getGfLanguages().stream().map(ResourceManager::codeToName).collect(Collectors.toList()));
        nativeBox.setItems(availableLanguages);
        foreignBox.setItems(availableLanguages);
    }

    public void addNewSession(){
        String sessionNative = nativeBox.getSelectionModel().getSelectedItem();
        String sessionForeign = foreignBox.getSelectionModel().getSelectedItem();
        GrammarManager.createSession(titleField.getText(), sessionNative, sessionForeign);

        refreshSessionsList();
    }

    private void refreshSessionsList(){
        //Clear previous content
        sessionsList.getChildren().clear();

        List<Session> sessions = GrammarManager.getSessions();
        for(int item = 0, row = 0; item < sessions.size(); item++, row++){
            Session session = sessions.get(item);

            //Add labels
            HBox box = new HBox();
            GridPane.setConstraints(box, 0, row++);
            sessionsList.getChildren().add(box);
            Label title = new Label(" - " + session.getTitle() + " - ");
            title.setId("sessionTitleLbl");
            box.getChildren().add(title);
            box.getChildren().add(new Label("Native: " + ResourceManager.codeToName(session.getNativeCode())));
            box.getChildren().add(new Label("& Foreign: " + ResourceManager.codeToName(session.getForeignCode())));

            //Add buttons
            box = new HBox();
            GridPane.setConstraints(box, 0, row);
            sessionsList.getChildren().add(box);

            //Launch button
            Button b = new Button("Launch session");
            b.setOnAction(event -> StartController.this.startPractice(session));
            b.setId("launchBtn");
            box.getChildren().add(b);

            //Delete button
            b = new Button("Delete");
            b.setOnAction(event -> StartController.this.deleteSession(session.getTitle()));
            b.setId("removeBtn");
            box.getChildren().add(b);
        }
    }

    private void startPractice(Session session){
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

        model.initialize(session);
        ((PracticeController)fxmlloader.getController()).init(model, pContent);
    }

    private void deleteSession(String sessionTitle){
        GrammarManager.removeSession(sessionTitle);
        refreshSessionsList();
    }
}
