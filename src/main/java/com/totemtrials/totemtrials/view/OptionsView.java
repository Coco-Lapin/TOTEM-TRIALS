package com.totemtrials.totemtrials.view;

import javafx.beans.binding.DoubleBinding;
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

        // Image de fond partagée pour les deux sliders
        Image sliderBg = new Image(
                getClass().getResourceAsStream("/images/buttons/bg-sound-slider.png")
        );

        // ── Music slider ─────────────────────────────────────────────────
        Label musicLabel = new Label("Game Music");
        musicLabel.getStyleClass().add("slider-label");

        musicSlider = new Slider(0, 100, 25);
        musicSlider.getStyleClass().add("jungle-slider");
        musicSlider.setId("music-slider");

        StackPane musicPane = buildSliderPane(
                musicSlider, sliderBg, stage,
                0.35,   // taille image
                0.26    // longueur slider
        );

        VBox musicBox = new VBox(-20, musicLabel, musicPane);
        musicBox.setAlignment(Pos.CENTER);

        // ── SFX slider ───────────────────────────────────────────────────
        Label sfxLabel = new Label("Sound Effects");
        sfxLabel.getStyleClass().add("slider-label");

        sfxSlider = new Slider(0, 100, 80);
        sfxSlider.getStyleClass().add("jungle-slider");
        sfxSlider.setId("sfx-slider");

        StackPane sfxPane = buildSliderPane(
                sfxSlider, sliderBg, stage,
                0.35,
                0.26
        );

        VBox sfxBox = new VBox(-20, sfxLabel, sfxPane);
        sfxBox.setAlignment(Pos.CENTER);

        // ── Back button ──────────────────────────────────────────────────
        backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");

        VBox layout = new VBox(40, musicBox, sfxBox, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setFillWidth(false);

        StackPane root = new StackPane(bg, layout);
        scene = new Scene(root, 600, 500);

        var css = getClass().getResource("/styleSheet/homepage.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    private StackPane buildSliderPane(Slider slider, Image trackImage, Stage stage,
                                      double imageWidthRatio,
                                      double sliderWidthRatio) {

        ImageView trackBg = new ImageView(trackImage);
        trackBg.fitWidthProperty().bind(stage.widthProperty().multiply(imageWidthRatio));
        trackBg.setPreserveRatio(true);
        trackBg.setMouseTransparent(true);

        DoubleBinding sliderW = stage.widthProperty().multiply(sliderWidthRatio);
        slider.minWidthProperty().bind(sliderW);
        slider.prefWidthProperty().bind(sliderW);
        slider.maxWidthProperty().bind(sliderW);

        slider.setShowTickLabels(true);
        slider.setShowTickMarks(false);
        slider.setMajorTickUnit(25);    // un trait majeur tous les 25
        slider.setMinorTickCount(4);    // 4 petits traits entre chaque majeur
        slider.setBlockIncrement(1);

        StackPane pane = new StackPane(trackBg, slider);
        pane.setAlignment(Pos.CENTER);
        pane.setPickOnBounds(false);

        // Compense la hauteur des ticks/labels affichés sous le slider
        slider.setTranslateY(8);   // ajuste : 8, 10, 12, 15 selon la taille de tes labels

        return pane;
    }

    public Scene  getScene()        { return scene; }
    public Slider getMusicSlider()  { return musicSlider; }
    public Slider getSfxSlider()    { return sfxSlider; }
    public Button getBackButton()   { return backButton; }
    public Slider getVolumeSlider() { return musicSlider; }
}