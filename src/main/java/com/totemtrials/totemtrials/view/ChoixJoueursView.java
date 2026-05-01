package com.totemtrials.totemtrials.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChoixJoueursView {

    private final StackPane backButton;
    private final ImageView btn2Joueurs;
    private final ImageView btn3Joueurs;
    private final ImageView btn4Joueurs;
    private final Scene  scene;
    final double PLAYER_BTN_RATIO = 0.25;

    public ChoixJoueursView(Stage stage, Image background) {
        ImageView bg = new ImageView(background);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        //----------------Creation des images pour les boutons----------
        btn2Joueurs = ViewUtils.createCroppedImageView(stage, "images/buttons/2Players.png",PLAYER_BTN_RATIO);
        btn3Joueurs = ViewUtils.createCroppedImageView(stage, "images/buttons/3Players.png",PLAYER_BTN_RATIO);
        btn4Joueurs = ViewUtils.createCroppedImageView(stage, "images/buttons/4Players.png",PLAYER_BTN_RATIO);

        backButton = ViewUtils.createBackButton(stage, 0.20);

        HBox boutons = new HBox(50, btn2Joueurs, btn3Joueurs, btn4Joueurs);
        boutons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(30,boutons ,backButton );
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(bg, layout);
        scene = new Scene(root, 600, 500);

        var css = getClass().getResource("/styleSheet/homepage.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    public Scene  getScene()       { return scene; }
    public StackPane getBackButton() { return backButton; }
    public ImageView getBtn2Joueurs() { return btn2Joueurs; }
    public ImageView getBtn3Joueurs() { return btn3Joueurs; }
    public ImageView getBtn4Joueurs() { return btn4Joueurs; }

}
