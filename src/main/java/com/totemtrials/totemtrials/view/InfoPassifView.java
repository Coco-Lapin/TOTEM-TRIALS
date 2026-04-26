package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.model.Jeton;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class InfoPassifView {

    private final Map<Jeton, ImageView> jetonViews = new LinkedHashMap<>();
    private final Map<Jeton, VBox> jetonGroupes = new LinkedHashMap<>();
    private final Button backButton;
    private final Scene scene;

    public InfoPassifView(Stage stage) {


        backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");


        StackPane root = new StackPane(backButton);

        scene = new Scene(root, 600, 500);

        var css = getClass().getResource("/com/totemtrials/totemtrials/styles/homepage.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

    }

    public Button getBackButton(){return backButton;}
}
