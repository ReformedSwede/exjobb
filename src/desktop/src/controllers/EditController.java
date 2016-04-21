package controllers;

import grammar.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Model;
import main.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class EditController {

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
        sessionTitle.setText("Native language: " + Utils.codeToName(model.getNativeLangCode()) +
                ", foreign language: " + Utils.codeToName(model.getForeignLangCode()));

        //Init both comboboxes with all parts of speech
        ObservableList<String> posList = FXCollections.observableArrayList();
        posList.addAll(model.getAllCategories());
        catField.setItems(posList);
        catBox.setItems(posList);
        catBox.setOnAction(event -> {
            ObservableList<String> list = FXCollections.observableArrayList();
            list.addAll(model.getAllWords(catBox.getValue()).stream()
                    .map(Word::getForeign).collect(Collectors.toList()));
            wordList.setItems(list);
        });
        catBox.setValue(catBox.getItems().get(0));

        //Init list of words
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(model.getAllWords(catBox.getValue()).stream()
                .map(Word::getForeign).collect(Collectors.toList()));
        wordList.setItems(list);
    }

    public void wordInListSelected(){
        if(wordList.getSelectionModel().getSelectedItem() != null) {
            removeBtn.setVisible(true);
            //Display info panel
            infoPanel.getChildren().clear();
            Word selectedWord = model.getWordByString(
                    catBox.getValue(),
                    wordList.getSelectionModel().getSelectedItem());
            List<String> info = selectedWord.getAllInflectionNames().stream()
                    .map(i -> selectedWord.getForeignInflectionFormByName(i) +
                            " = " + selectedWord.getNativeInflectionFormByName(i))
                    .collect(Collectors.toList());
            infoPanel.getChildren().addAll(info.stream().map(Label::new).collect(Collectors.toList()));
        }
    }

    public void addNewWord(){
        //Add to model
        model.addNewWord(catField.getValue(), nativeField.getText().trim(), foreignField.getText().trim());
        openConfirmDialog();

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
        model.removeWord(catBox.getValue(), wordList.getSelectionModel().getSelectedItem());

        //Clean up gui
        infoPanel.getChildren().clear();
        refreshWordList();
        removeBtn.setVisible(false);
    }


    private void openConfirmDialog() {
        try {
        Stage dialog = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view/confirm-dialog.fxml"));
        dialog.setTitle("Confirm");
        Scene scene = new Scene(root, 400, 200);
        dialog.setScene(scene);
        dialog.show();

        //TODO If user clicks OK, do nothing
        //TODO If user clicks edit, let him edit all fields, then rewrite new data to GF files

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
