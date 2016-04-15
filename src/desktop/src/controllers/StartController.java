package controllers;

import grammar.GrammarManager;
import main.Utils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.util.Pair;
import main.Model;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StartController implements Initializable {

    private Model model = new Model();

    //UI components
    public Pane pContent;
    public TilePane sessionsGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Pair<String, String>> sessions = GrammarManager.getSessions();

        sessions.forEach((session) -> {
            HBox box = new HBox();
            box.getChildren().add(new Label(Utils.codeToName(session.getKey())));
            box.getChildren().add(new Label(Utils.codeToName(session.getValue())));
            Button b = new Button("Launch session");
            b.setOnAction(event -> startPractice(session.getKey(), session.getValue()));
            box.getChildren().add(b);
            sessionsGrid.getChildren().add(box);
        });
    }

    public void startPractice(String nativeLang, String foreignLang){
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
}
