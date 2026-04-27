package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.view.HomePageView;
import com.totemtrials.totemtrials.view.OptionsView;
import javafx.scene.media.MediaPlayer;

public class OptionsController {

    public OptionsController(OptionsView view, HomePageView homeView) {
        // Le MediaPlayer est géré dans TotemTrialsApp et passé ici si nécessaire.
        // Pour l'instant le binding volume n'est pas connecté au player —
        // voir TotemTrialsApp pour injecter le MediaPlayer.

        view.getBackButton().setOnAction(_ ->
            SceneManager.show(homeView.getScene(), "Menu principal")
        );
    }

    /** Surcharge permettant de brancher le volume sur un MediaPlayer existant. */
    public OptionsController(OptionsView view, HomePageView homeView, MediaPlayer player) {
        this(view, homeView);
        player.volumeProperty().bind(view.getVolumeSlider().valueProperty().divide(100));
    }
}
