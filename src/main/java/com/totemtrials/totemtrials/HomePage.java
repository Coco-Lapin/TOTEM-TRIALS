package com.totemtrials.totemtrials;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePage extends DefaultPage {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        loadBackground("Images/backgroundMenu.png");
        if (backgroundImage == null) return;

        loadMusic("sounds/Agrual.mp3");

        // ------------------- BACKGROUND -------------------
        ImageView imageView = new ImageView(backgroundImage);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());
        imageView.setPreserveRatio(false);

        // ------------------- TITRE + BOUTONS -------------------
        ImageView titre        = createCroppedImageView("Images/Titre-sora.png",           0.45);
        ImageView playbutton   = createCroppedImageView("Images/buttons/Start-sora.png",   0.45);
        ImageView optionbutton = createCroppedImageView("Images/buttons/Options-sora.png", 0.38);
        ImageView quitbutton   = createCroppedImageView("Images/buttons/Exit-sora.png",    0.28);

        // ------------------- BOUTON TEST ENDPAGE -------------------
        ImageView endGameButton = createCroppedImageView("Images/buttons/Start-sora.png", 0.3);

        for (ImageView iv : new ImageView[]{playbutton, optionbutton, quitbutton, endGameButton}) {
            iv.setCursor(Cursor.HAND);
            iv.setOnMouseEntered(_ -> iv.setOpacity(0.8));
            iv.setOnMouseExited(_  -> iv.setOpacity(1.0));
        }

        endGameButton.setOnMouseClicked(_ -> {
            EndPage endPage = new EndPage();
            try {
                endPage.start(stage);
            } catch (Exception e) {
                System.err.println("Erreur navigation EndPage : " + e.getMessage());
            }
        });

        // ------------------- LAYOUT -------------------
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 600, 500);

        var cssUrl = getClass().getResource("styles/homepage.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

        VBox fenetreBouton = new VBox(10);
        fenetreBouton.getChildren().addAll(titre, playbutton, endGameButton, optionbutton, quitbutton);
        fenetreBouton.setAlignment(Pos.CENTER);
        root.getChildren().add(fenetreBouton);

        // ------------------- ACTIONS -------------------
        quitbutton.setOnMouseClicked(_ -> Platform.exit());

        Scene optionsScene = option(scene);
        optionbutton.setOnMouseClicked(_ -> {
            stage.setScene(optionsScene);
            applyFullScreen("OPTIONS");
        });

        // ------------------- STAGE -------------------
        stage.setScene(scene);
        applyFullScreen("Menu principal");
        stage.show();
    }

    public Scene option(Scene menuScene) {
        ImageView imageViewOption = new ImageView(backgroundImage);
        imageViewOption.fitWidthProperty().bind(stage.widthProperty());
        imageViewOption.fitHeightProperty().bind(stage.heightProperty());
        imageViewOption.setPreserveRatio(false);

        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");

        Slider volumeSlider = new Slider();
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(25);
        volumeSlider.prefWidthProperty().bind(stage.widthProperty().multiply(0.2));
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setBlockIncrement(1);

        VBox fenetreOption = new VBox(10);
        fenetreOption.getChildren().addAll(volumeSlider, backButton);
        fenetreOption.setAlignment(Pos.CENTER);
        fenetreOption.setFillWidth(false);

        StackPane rootOption = new StackPane(imageViewOption);
        rootOption.getChildren().add(fenetreOption);

        Scene optionsScene = new Scene(rootOption, 600, 500);

        var cssUrl = getClass().getResource("styles/homepage.css");
        if (cssUrl != null) optionsScene.getStylesheets().add(cssUrl.toExternalForm());

        if (mediaPlayer != null) {
            mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty().divide(100));
        }

        backButton.setOnAction(_ -> {
            stage.setScene(menuScene);
            Platform.runLater(() -> applyFullScreen("Menu principal"));
        });

        return optionsScene;
    }
}