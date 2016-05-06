package controllers;

import grammar.Word;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.InflectionCallback;
import main.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DialogController {
    private boolean editing = false;

    private Word word;
    private InflectionCallback callback;
    private Stage thisStage;
    private List<TextField> nativeFields = new ArrayList<>();
    private List<TextField> foreignFields = new ArrayList<>();

    public Label catLbl;
    public GridPane table;
    public Button editBtn;

    public void init(Word word, InflectionCallback callback, Stage thisStage){
        this.word = word;
        this.callback = callback;
        this.thisStage = thisStage;

        //Add labels
        catLbl.setText("Category: " + word.getCategory());
        Label labelPtr = new Label(word.getNativeLanguage());
        GridPane.setConstraints(labelPtr, 1, 0);
        table.getChildren().add(labelPtr);
        labelPtr = new Label(word.getForeignLanguage());
        GridPane.setConstraints(labelPtr, 2, 0);
        table.getChildren().add(labelPtr);

        //Add data to table
        int row = 1;
        for(String i : ResourceManager.getInflectionRealNamesByCat(word.getCategory())){
            labelPtr = new Label(i);
            GridPane.setConstraints(labelPtr, 0, row);
            table.getChildren().add(labelPtr);

            TextField tf = new TextField(word.getNativeInflectionFormByName(i));
            tf.setEditable(false);
            nativeFields.add(tf);
            GridPane.setConstraints(tf, 1, row);
            table.getChildren().add(tf);

            tf = new TextField(word.getForeignInflectionFormByName(i));
            tf.setEditable(false);
            foreignFields.add(tf);
            GridPane.setConstraints(tf, 2, row);
            table.getChildren().add(tf);
            row++;
        }
    }

    public void ok(){
        if(editing)
            callback.call(word.getCategory(), word.getFunction(),
                    nativeFields.stream().map((textField) ->
                            textField.getText().trim()).collect(Collectors.toList()),
                    foreignFields.stream().map((textField1) ->
                            textField1.getText().trim()).collect(Collectors.toList()));
        thisStage.close();
    }

    public void edit(){
        for(TextField tf : nativeFields)
            tf.setEditable(true);
        for(TextField tf : foreignFields)
            tf.setEditable(true);
        editing = true;
        editBtn.setDisable(true);
    }
}
