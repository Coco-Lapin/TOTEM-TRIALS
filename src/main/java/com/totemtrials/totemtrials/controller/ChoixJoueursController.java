package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.Partie;
import com.totemtrials.totemtrials.models.GameConfig;
import com.totemtrials.totemtrials.view.ChoixJetonsView;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.HomePageView;

public class ChoixJoueursController {
    private GameConfig gm;
    public ChoixJoueursController(ChoixJoueursView view, Partie model, HomePageView homeView,GameConfig gm) {
            this.gm = gm;
        view.getBackButton().setOnAction(_ ->
            SceneManager.show(homeView.getScene(), "Menu principal")
        );

        view.getBtn2Joueurs().setOnMouseClicked(_ -> goChoixJetons(2, model, view, homeView));
        view.getBtn3Joueurs().setOnMouseClicked(_ -> goChoixJetons(3, model, view, homeView));
        view.getBtn4Joueurs().setOnMouseClicked(_ -> goChoixJetons(4, model, view, homeView));
    }

    private void goChoixJetons(int count, Partie model, ChoixJoueursView view, HomePageView homeView) {
        model.initJoueurs(count);
        ChoixJetonsView jetonsView = new ChoixJetonsView(
                SceneManager.getStage(),
                homeView.getBackground(),
                model.getJetonsDisponibles()
        );
        new ChoixJetonsController(jetonsView, model, view, homeView ,this.gm );
        SceneManager.show(jetonsView.getScene(), "Choix des jetons");
    }
}
