package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.view.HomePageView;
import com.totemtrials.totemtrials.view.OptionsView;
import javafx.scene.media.MediaPlayer;

public class OptionsController {

    public OptionsController(OptionsView view, HomePageView homeView) {
        // The MediaPlayer is managed in TotemTrialsApp and passed here if necessary.

        // Currently, the volume binding is not connected to the player —

        // See TotemTrialsApp to inject the MediaPlayer.

        view.getBackButton().setOnAction(_ ->
            SceneManager.show(homeView.getScene(), "Menu principal")
        );
    }

    /** Overload allowing the volume to be connected to an existing Media Player. */    public OptionsController(OptionsView view, HomePageView homeView, MediaPlayer player) {
        this(view, homeView);
        player.volumeProperty().bind(view.getVolumeSlider().valueProperty().divide(100));
    }
}
