package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.view.HomePageView;
import com.totemtrials.totemtrials.view.OptionsView;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;

public class OptionsController {

    // ── Menu principal ──────────────────────────────────────────────────────
    public OptionsController(OptionsView view, HomePageView homeView) {
        view.getBackButton().setOnMouseClicked(_ ->
                SceneManager.show(homeView.getScene(), "Menu principal")
        );
    }

    public OptionsController(OptionsView view, HomePageView homeView, MediaPlayer player) {
        this(view, homeView);
        if (player != null) bindVolume(view, player);
    }

    // ── Plateau (FXML) ──────────────────────────────────────────────────────
    public OptionsController(OptionsView view, Scene previousScene, String previousTitle) {
        view.getBackButton().setOnMouseClicked(_ ->
                SceneManager.show(previousScene, previousTitle)
        );
    }

    public OptionsController(OptionsView view, Scene previousScene, String previousTitle, MediaPlayer player) {
        this(view, previousScene, previousTitle);
        if (player != null) bindVolume(view, player);
    }

    // ── Binding audio ───────────────────────────────────────────────────────
    private void bindVolume(OptionsView view, MediaPlayer player) {
        // --- Music slider ---
        // Initialise sur le volume courant avant de binder (évite le reset à 25%)
        view.getMusicSlider().setValue(player.getVolume() * 100);
        player.volumeProperty().bind(
                view.getMusicSlider().valueProperty().divide(100)
        );

        // --- SFX slider ---
        // Initialise sur le volume SFX courant stocké dans SceneManager
        view.getSfxSlider().setValue(SceneManager.getSfxVolume() * 100);
        // Chaque changement du slider met à jour SceneManager.sfxVolume
        view.getSfxSlider().valueProperty().addListener((obs, oldVal, newVal) ->
                SceneManager.setSfxVolume(newVal.doubleValue() / 100)
        );
    }
}