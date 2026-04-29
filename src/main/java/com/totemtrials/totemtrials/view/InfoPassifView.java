package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.model.Jeton;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;

public class InfoPassifView {

    private final Map<Jeton, ImageView> jetonViews = new LinkedHashMap<>();
    private final Map<Jeton, VBox> jetonGroupes = new LinkedHashMap<>();
    private final Button backButton;
    private final Scene scene;

    public InfoPassifView(Stage stage, Image background, Jeton[] jetonsDisponibles) {

        ImageView bg = new ImageView(background);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");
        backButton.setAlignment(Pos.BOTTOM_RIGHT);

        //Creating the table
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        for(Jeton j : jetonsDisponibles){
            HBox hBox = new HBox(20); // Espacement de 20px entre les colonnes
            hBox.setAlignment(Pos.CENTER);

            ImageView base = ViewUtils.createCroppedImageView(stage, j.getImagePath(), 0.10);
            Label passif = new Label(j.getPassif());
            passif.setFont(Font.font("System", FontWeight.BOLD, 18));
            passif.setTextFill(Color.WHITE);

            hBox.getChildren().addAll(base, passif);

            // Adding the HBox to the VBox
            vBox.getChildren().add(hBox);
        }

        vBox.getChildren().add(backButton);
        StackPane root = new StackPane(bg,vBox);

        scene = new Scene(root, 600, 500);

        var css = getClass().getResource("/com/totemtrials/totemtrials/styles/homepage.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

    }

    public Scene                 getScene()            { return scene; }
    public Button getBackButton(){return backButton;}
}
