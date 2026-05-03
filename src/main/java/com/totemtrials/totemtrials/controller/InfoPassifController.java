package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.view.ChoixJetonsView;
import com.totemtrials.totemtrials.view.InfoPassifView;
import com.totemtrials.totemtrials.view.OptionsView;
import javafx.scene.Scene;

public class InfoPassifController {

    public InfoPassifController(InfoPassifView view, ChoixJetonsView Cview){

        view.getBackButton().setOnMouseClicked(_ ->
                SceneManager.show(Cview.getScene(), "Token choice")
        );
    }

    public InfoPassifController(InfoPassifView view, Scene previousScene) {
        view.getBackButton().setOnMouseClicked(_ ->
                SceneManager.show(previousScene, "Show passif")
        );
    }
}