package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.view.HomePageView;
import com.totemtrials.totemtrials.view.OptionsView;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;

public class OptionsController {

    // ── Menu principal ──────────────────────────────────────────────────────
    public OptionsController(OptionsView view, HomePageView homeView) {
        view.getBackButton().setOnAction(_ ->
                SceneManager.show(homeView.getScene(), "Menu principal")
        );
    }

    public OptionsController(OptionsView view, HomePageView homeView, MediaPlayer player) {
        this(view, homeView);
        if (player != null) bindVolume(view, player);
    }

    // ── Plateau (FXML) ──────────────────────────────────────────────────────
    public OptionsController(OptionsView view, Scene previousScene, String previousTitle) {
        view.getBackButton().setOnAction(_ ->
                SceneManager.show(previousScene, previousTitle)
        );
    }

    public OptionsController(OptionsView view, Scene previousScene, String previousTitle, MediaPlayer player) {
        this(view, previousScene, previousTitle);
        if (player != null) bindVolume(view, player);
    }

    // ── Helper ──────────────────────────────────────────────────────────────
    /**
     * Initialise le slider sur le volume ACTUEL du player AVANT de binder.
     * Sans ça, le bind() fire immédiatement et remet le volume à 25 (valeur
     * par défaut du slider).
     */
    private void bindVolume(OptionsView view, MediaPlayer player) {
        // 1. Positionne le slider sur le volume courant
        view.getVolumeSlider().setValue(player.getVolume() * 100);
        // 2. Maintenant le bind ne change rien car slider = volume actuel
        player.volumeProperty().bind(
                view.getVolumeSlider().valueProperty().divide(100)
        );
    }
}