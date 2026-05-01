package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.view.ChoixJetonsView;
import com.totemtrials.totemtrials.view.InfoPassifView;

public class InfoPassifController {

    public InfoPassifController(InfoPassifView view, ChoixJetonsView Cview){

<<<<<<< HEAD
        view.getBackButton().setOnMouseClicked(_ ->
=======
        view.getBackButton().setOnAction(_ ->
>>>>>>> feat/menus
                SceneManager.show(Cview.getScene(), "Token choice")
        );

    }
<<<<<<< HEAD
}
=======
}
>>>>>>> feat/menus
