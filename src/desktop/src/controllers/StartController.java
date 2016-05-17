package controllers;

import grammar.GrammarManager;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import main.ResourceManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import main.Model;
import main.Session;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for the start window. Contains methods for creating new sessions and launching old ones
 */
public class StartController implements Initializable {

    private Model model = new Model();

    //UI components
    @FXML
    Pane pContent;
    @FXML
    GridPane sessionsList;
    @FXML
    ComboBox<String> nativeBox;
    @FXML
    ComboBox<String> foreignBox;
    @FXML
    TextField titleField;
    @FXML
    Button addNew;

    /**
     * Initializes the view
     * @param location Path of the root object
     * @param resources Resources for the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set list of sessions and lists of languages
        refreshSessionsList();
        ObservableList<String> availableLanguages = FXCollections.observableArrayList();
        availableLanguages.addAll(ResourceManager.getGfLanguages().stream().map(ResourceManager::codeToName).collect(Collectors.toList()));
        nativeBox.setItems(availableLanguages);
        foreignBox.setItems(availableLanguages);
    }

    /**
     * Called when the user lauches a session
     * @param session The session to launch
     */
    private void launchSession(Session session){
        //Load FXMl
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

    /**
     * Called when the user clicked the delete button on a session. Asks the user for confirmation, then deletes it
     * @param sessionTitle The title of the session to be removed
     */
    private void deleteSession(String sessionTitle){
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this session?");
        confirmDialog.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> {
                GrammarManager.removeSession(sessionTitle);
                refreshSessionsList();
            });
    }

    /**
     * Called when the user clicks the add button to add a new session
     */
    public void addNewSession(){
        //Create new session
        String sessionNative = nativeBox.getSelectionModel().getSelectedItem();
        String sessionForeign = foreignBox.getSelectionModel().getSelectedItem();
        GrammarManager.createSession(titleField.getText(), sessionNative, sessionForeign);

        //Update view
        refreshSessionsList();
    }

    /**
     * Updates the list of sessions
     */
    private void refreshSessionsList(){
        //Clear previous content
        sessionsList.getChildren().clear();

        //Fill view with new content
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
            b.setOnAction(event -> StartController.this.launchSession(session));
            b.setId("launchBtn");
            box.getChildren().add(b);

            //Delete button
            b = new Button("Delete");
            b.setOnAction(event -> StartController.this.deleteSession(session.getTitle()));
            b.setId("removeBtn");
            box.getChildren().add(b);
        }
    }
}
