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

    private final Button backButton;
    private final ImageView btn2Joueurs;
    private final ImageView btn3Joueurs;
    private final ImageView btn4Joueurs;
    private final Scene  scene;

    public ChoixJoueursView(Stage stage, Image background) {
        ImageView bg = new ImageView(background);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        //----------------Creation des images pour les boutons----------
        btn2Joueurs = ViewUtils.createCroppedImageView(stage,"/com/totemtrials/totemtrials/Images/buttons/2Players.png",0.3);
        btn3Joueurs = ViewUtils.createCroppedImageView(stage,"/com/totemtrials/totemtrials/Images/buttons/3Players.png",0.3);
        btn4Joueurs = ViewUtils.createCroppedImageView(stage,"/com/totemtrials/totemtrials/Images/buttons/4Players.png",0.25);

        backButton   = new Button("BACK");

        backButton.getStyleClass().add("back-button");

        HBox boutons = new HBox(10, btn2Joueurs, btn3Joueurs, btn4Joueurs);
        boutons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10, backButton, boutons);
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(bg, layout);
        scene = new Scene(root, 600, 500);

        var css = getClass().getResource("/com/totemtrials/totemtrials/styles/homepage.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    public Scene  getScene()       { return scene; }
    public Button getBackButton()  { return backButton; }
    public ImageView getBtn2Joueurs() { return btn2Joueurs; }
    public ImageView getBtn3Joueurs() { return btn3Joueurs; }
    public ImageView getBtn4Joueurs() { return btn4Joueurs; }

}
