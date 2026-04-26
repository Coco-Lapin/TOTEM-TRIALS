package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.view.ChoixJetonsView;
import com.totemtrials.totemtrials.view.InfoPassifView;

public class InfoPassifController {

    public InfoPassifController(InfoPassifView view, ChoixJetonsView Cview){

        view.getBackButton().setOnAction(_ ->
                SceneManager.show(Cview.getScene(), "Token choice")
        );

    }
}
