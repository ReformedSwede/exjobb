package controllers;

import grammar.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.InflectionCallback;
import main.Model;
import main.ResourceManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the Edit frame. Contains methods for handling displaying of words and addition of new words.
 */
public class EditController implements InflectionCallback {

    private Model model;

    //UI components
    private Pane pContent;
    @FXML
    Label sessionTitle;
    @FXML
    ComboBox<String> catBox;
    @FXML
    ListView<String> wordList;
    @FXML
    ComboBox<String> catField;
    @FXML
    TextField nativeField;
    @FXML
    TextField foreignField;
    @FXML
    Button removeBtn;
    @FXML
    VBox infoPanel;

    /**
     * Initalized the view.
     * @param model The model (M in MVC)
     * @param content The JavaFX pane containing the view.
     */
    void init(Model model, Pane content){
        //Set important vars
        this.model = model;
        pContent = content;

        //Set windows size
        pContent.getScene().getWindow().setHeight(700);
        pContent.getScene().getWindow().setWidth(1200);

        //Init components
        sessionTitle.setText("Native language: " + ResourceManager.codeToName(model.getNativeLangCode()) +
                ", foreign language: " + ResourceManager.codeToName(model.getForeignLangCode()));

        //Init both comboboxes with all parts of speech
        ObservableList<String> posList = FXCollections.observableArrayList();
        posList.addAll(ResourceManager.getPartOfSpeechCats());
        catField.setItems(posList);
        catBox.setItems(posList);
        catBox.setOnAction(event -> refreshWordList());
        catBox.setValue(catBox.getItems().get(0));

        //Init list of words
        refreshWordList();
    }

    /**
     * Is run whenever the user selects a word in the list.
     * Displays information about the selected item.
     */
    public void wordInListSelected(){
        if(wordList.getSelectionModel().getSelectedItem() != null) {
            removeBtn.setVisible(true);
            infoPanel.getChildren().clear();

            //Fill info panel with content
            Word selectedWord = model.getWordByString(
                    catBox.getValue(),
                    wordList.getSelectionModel().getSelectedItem());
            List<String> info = ResourceManager.getInflectionRealNamesByCat(selectedWord.getCategory()).stream()
                    .map(infl -> selectedWord.getForeignInflectionFormByName(infl) + " = " +
                            selectedWord.getNativeInflectionFormByName(infl))
                    .collect(Collectors.toList());
            infoPanel.getChildren().addAll(info.stream().map(Label::new).collect(Collectors.toList()));
        }
    }

    /**
     * Is run whenever the user clicks the ADD button in order to add a new word to the database.
     */
    public void addNewWord(){
        if(catField.getValue() == null) return;

        //Add to model
        Word addedWord =
                model.addNewWord(catField.getValue(), nativeField.getText().trim(), foreignField.getText().trim());
        if(addedWord == null) {
            infoPanel.getChildren().add(new Label("Failed to add " + nativeField.getText()));
            foreignField.clear();
            nativeField.clear();
            removeBtn.setVisible(false);
            return;
        }
        openConfirmDialog(addedWord);

        //Refresh view
        if(catBox.getValue().equals(catField.getValue()))
            refreshWordList();
        else
            catBox.setValue(catField.getValue());

        //Clean up gui
        foreignField.clear();
        nativeField.clear();
        removeBtn.setVisible(false);
    }

    /**
     * Is run whenever the user clicks the REMOVE button to remove a word form the database
     */
    public void removeWord(){
        //Remove from file
        model.removeWord(model.getWordByString(
                catBox.getValue(), wordList.getSelectionModel().getSelectedItem()).getFunction());

        //Clean up gui
        infoPanel.getChildren().clear();
        refreshWordList();
        removeBtn.setVisible(false);
    }

    /**
     * Is run whenever a new word has been inserted into the database
     * @param word The inserted word
     */
    private void openConfirmDialog(Word word) {
        //Load FXML file containing the view
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/resources/view/confirm-dialog.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        try {
            fxmlLoader.setRoot(
                    fxmlLoader.load(getClass().getResource("/resources/view/confirm-dialog.fxml").openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //init scene and stage
        Scene scene = new Scene(fxmlLoader.getRoot(), 800, 400);
        scene.getStylesheets().add("/resources/css/dialog-style.css");

        Stage dialog = new Stage();
        dialog.setTitle("Does this look alright?");
        dialog.setScene(scene);
        dialog.show();

        //Init controller
        ((DialogController)fxmlLoader.getController()).init(word, this, dialog);
    }

    /**
     * Refreshed the contents of the list of words.
     */
    private void refreshWordList(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(model.getAllWords(catBox.getValue()).stream()
                .map(Word::getForeign).collect(Collectors.toList()));
        wordList.setItems(list);
    }

    /**
     * This is a callback method for the confirm dialog. If the user edits the words in the confirm dialog,
     * then clicks OK, this method is called.
     * @param category The category of the inserted word
     * @param function The function of the inserted word
     * @param foreignInflections A list of native inflection forms
     * @param nativeInflections A list of foreign inflection forms
     */
    @Override
    public void call(String category, String function, List<String> nativeInflections, List<String> foreignInflections) {
        model.removeWord(function);
        model.addWordWithInflections(category, nativeInflections, foreignInflections);

        //Refresh view
        refreshWordList();
    }

    /****Navigation Methods****/

    /**
     * Exit the edit window and go to start window
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

    /**
     * Move t the practice frame
     */
    public void practice(){
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
        ((PracticeController)fxmlloader.getController()).init(model, pContent);
    }
}
