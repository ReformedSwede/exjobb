package controllers;

import grammar.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import main.Model;
import main.ResourceManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for the Edit frame. Contains methods for handling displaying of words and addition of new words.
 */
public class EditController{

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
        List<TextField> nativeFields = new ArrayList<>();
        List<TextField> foreignFields = new ArrayList<>();
        final boolean[] editing = {false};

        // Create a custom dialog.
        Dialog<Pair<List<String>, List<String>>> dialog = new Dialog<>();
        dialog.setTitle("Confirm or edit " + word.getCategory());
        dialog.getDialogPane().getStylesheets().add("/resources/css/dialog-style.css");

        // Set the button types.
        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OTHER);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, editButtonType);

        // Create all labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.add(new Label(word.getNativeLanguage()), 1, 0);
        grid.add(new Label(word.getForeignLanguage()), 2, 0);
        int row = 1;
        for(String s : ResourceManager.getInflectionRealNamesByCat(word.getCategory())){
            grid.add(new Label(s), 0, row);
            TextField tf = new TextField(word.getNativeInflectionFormByName(s));
            tf.setEditable(false);
            grid.add(tf, 1, row);
            nativeFields.add(tf);
            tf = new TextField(word.getForeignInflectionFormByName(s));
            tf.setEditable(false);
            grid.add(tf, 2, row);
            foreignFields.add(tf);
            row++;
        }
        dialog.getDialogPane().setContent(grid);

        // Set edit button onClick
        Node editButton = dialog.getDialogPane().lookupButton(editButtonType);
        editButton.setDisable(false);
        editButton.setOnMouseClicked(event -> {
            for(TextField tf : nativeFields)
                tf.setEditable(true);
            for(TextField tf : foreignFields)
                tf.setEditable(true);
            editing[0] = true;
            editButton.setDisable(true);
        });

        // Convert the result to a pair when ok button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                if(editing[0]){
                    return new Pair<>(nativeFields.stream()
                                            .map((textField) -> textField.getText().trim())
                                            .collect(Collectors.toList()),
                                    foreignFields.stream()
                                            .map((textField1) -> textField1.getText().trim())
                                            .collect(Collectors.toList()));
                }
                return new Pair<>(null, null);
            }
            return null;
        });

        Optional<Pair<List<String>, List<String>>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            if(pair.getValue() != null) {
                model.removeWord(word.getFunction());
                model.addWordWithInflections(word.getCategory(),
                        pair.getKey(), pair.getValue());

                //Refresh view
                refreshWordList();
            }
        });
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
