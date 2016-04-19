package controllers;

import grammar.Word;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import main.Model;
import main.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

public class EditController {

    private Model model;

    //UI components
    public Pane pContent;
    public Label sessionTitle;
    public ComboBox<String> posBox;
    public ListView<String> wordList;
    public TextField posField;
    public TextField nativeField;
    public TextField foreignField;

    public void init(Model model, Pane content){
        //Set important vars
        this.model = model;
        pContent = content;

        //Set windows size
        pContent.getScene().getWindow().setHeight(700);
        pContent.getScene().getWindow().setWidth(1000);

        //Init components
        sessionTitle.setText("Current session: " + Utils.codeToName(model.getNativeLangCode()) +
                " speaker practising " + Utils.codeToName(model.getForeignLangCode()));

        ObservableList<String> posList = FXCollections.observableArrayList();
        posList.addAll(model.getAllPartOfSpeech());
        posBox.setItems(posList);
        posBox.setOnAction(event -> {
            ObservableList<String> list = FXCollections.observableArrayList();
            list.addAll(model.getAllWords(posBox.getValue()).stream()
                    .map(Word::getForeign).collect(Collectors.toList()));
            wordList.setItems(list);
        });
        posBox.setValue(posBox.getItems().get(0));

        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(model.getAllWords(posBox.getValue()).stream()
                .map(Word::getForeign).collect(Collectors.toList()));
        wordList.setItems(list);
    }

    public void addNewWord(){
        //Add to model
        model.addNewWord(posField.getText().trim(), nativeField.getText().trim(), foreignField.getText().trim());

        //Refresh view
        posBox.setValue(posField.getText().trim());

        posField.clear();
        foreignField.clear();
        nativeField.clear();
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
