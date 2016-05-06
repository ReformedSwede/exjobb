package controllers;

import grammar.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class EditController implements InflectionCallback {

    private Model model;

    //UI components
    public Pane pContent;
    public Label sessionTitle;
    public ComboBox<String> catBox;
    public ListView<String> wordList;
    public ComboBox<String> catField;
    public TextField nativeField;
    public TextField foreignField;
    public Button removeBtn;
    public VBox infoPanel;

    public void init(Model model, Pane content){
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

    public void wordInListSelected(){
        if(wordList.getSelectionModel().getSelectedItem() != null) {
            removeBtn.setVisible(true);

            //Display info panel
            infoPanel.getChildren().clear();
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

    public void addNewWord(){
        if(catField.getValue() == null) return;

        //Add to model
        Word addedWord =
                model.addNewWord(catField.getValue(), nativeField.getText().trim(), foreignField.getText().trim());
        if(addedWord == null)
            return;        //TODO Notify user that insertion failed!
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

    public void removeWord(){
        //Remove from file
        model.removeWord(catBox.getValue(),
                model.getWordByString(catBox.getValue(), wordList.getSelectionModel().getSelectedItem()).getFunction());

        //Clean up gui
        infoPanel.getChildren().clear();
        refreshWordList();
        removeBtn.setVisible(false);
    }


    private void openConfirmDialog(Word word) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/resources/view/confirm-dialog.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        try {
            fxmlLoader.setRoot(
                    fxmlLoader.load(getClass().getResource("/resources/view/confirm-dialog.fxml").openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(fxmlLoader.getRoot(), 800, 400);
        scene.getStylesheets().add("/resources/css/dialog-style.css");

        Stage dialog = new Stage();
        dialog.setTitle("Does this look alright?");
        dialog.setScene(scene);
        dialog.show();

        ((DialogController)fxmlLoader.getController()).init(word, this, dialog);
    }

    private void refreshWordList(){
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(model.getAllWords(catBox.getValue()).stream()
                .map(Word::getForeign).collect(Collectors.toList()));
        wordList.setItems(list);
    }

    /**
     * This is a callback method for the confirm dialog. If the user edits the words in the confirm dialog,
     * This method is called.
     * @param foreignInflections
     * @param nativeInflections
     */
    @Override
    public void call(String category, String function, List<String> nativeInflections, List<String> foreignInflections) {
        model.removeWord(category, function);
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

    public void practice(){
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
