package controllers;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import main.Model;
import main.Utils;

import java.util.stream.Collectors;

public class EditController {

    private Model model;

    //UI components
    public Pane pContent;
    public Label sessionTitle;
    public TilePane posList;
    public TilePane wordList;

    public void exitSession(){

    }

    public void init(Model model, Pane content){
        this.model = model;
        pContent = content;

        sessionTitle.setText("Current session: " + Utils.codeToName(model.getNativeLangCode()) +
                " speaker practising " + Utils.codeToName(model.getForeignLangCode()));
        model.getAllPartOfSpeech().forEach(pos -> posList.getChildren().add(new Label(pos)));
        model.getAllForeignWords().forEach(word -> wordList.getChildren().add(new Label(word)));
    }

}
