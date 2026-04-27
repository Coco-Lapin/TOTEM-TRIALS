package com.totemtrials.totemtrials.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OptionsView {

    private final Slider musicSlider;
    private final Slider sfxSlider;
    private final Button backButton;
    private final Scene  scene;

    public OptionsView(Stage stage, Image background) {
        ImageView bg = new ImageView(background);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        // ── Music slider ─────────────────────────────────────────────────────
        Label musicLabel = new Label("Game Music");
        musicLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 16px; -fx-text-fill: #f5a623;");

        musicSlider = new Slider(0, 100, 25);
        musicSlider.prefWidthProperty().bind(stage.widthProperty().multiply(0.25));
        musicSlider.setShowTickLabels(true);
        musicSlider.setShowTickMarks(true);
        musicSlider.setMajorTickUnit(25);
        musicSlider.setBlockIncrement(1);
        musicSlider.setStyle("-fx-accent: #f5a623;");

        VBox musicBox = new VBox(6, musicLabel, musicSlider);
        musicBox.setAlignment(Pos.CENTER);

        // ── SFX slider ───────────────────────────────────────────────────────
        Label sfxLabel = new Label("Sound Effects");
        sfxLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 16px; -fx-text-fill: #f5a623;");

        sfxSlider = new Slider(0, 100, 80);
        sfxSlider.prefWidthProperty().bind(stage.widthProperty().multiply(0.25));
        sfxSlider.setShowTickLabels(true);
        sfxSlider.setShowTickMarks(true);
        sfxSlider.setMajorTickUnit(25);
        sfxSlider.setBlockIncrement(1);
        sfxSlider.setStyle("-fx-accent: #f5a623;");

        VBox sfxBox = new VBox(6, sfxLabel, sfxSlider);
        sfxBox.setAlignment(Pos.CENTER);

        // ── Back button ──────────────────────────────────────────────────────
        backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");

        // ── Layout ───────────────────────────────────────────────────────────
        VBox layout = new VBox(30, musicBox, sfxBox, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setFillWidth(false);

        StackPane root = new StackPane(bg, layout);
        scene = new Scene(root, 600, 500);

        var css = getClass().getResource("/styleSheet/homepage.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    public Scene  getScene()        { return scene; }
    public Slider getMusicSlider()  { return musicSlider; }
    public Slider getSfxSlider()    { return sfxSlider; }
    public Button getBackButton()   { return backButton; }

    /** Rétrocompatibilité — pointe sur le slider musique */
    public Slider getVolumeSlider() { return musicSlider; }
}