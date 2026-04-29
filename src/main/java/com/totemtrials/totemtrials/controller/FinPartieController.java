package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.model.Partie;
import com.totemtrials.totemtrials.model.StatistiquesPartie;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.FinPartieView;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.application.Platform;

public class FinPartieController {

    public FinPartieController(FinPartieView view, Partie model, HomePageView homeView, StatistiquesPartie stats) {

        // Play Again → choix du nombre de joueurs
        view.getBtnRejouer().setOnMouseClicked(_ -> {
            ChoixJoueursView cjv = new ChoixJoueursView(SceneManager.getStage(), homeView.getBackground());
            new ChoixJoueursController(cjv, model, homeView);
            SceneManager.show(cjv.getScene(), "Choix des joueurs");
        });

        view.getBtnStats().setOnMouseClicked(_ -> view.toggleStats(true));

        view.getBtnQuitter().setOnMouseClicked(_ -> Platform.exit());

        view.getBtnFermerStats().setOnMouseClicked(_ -> view.toggleStats(false));
    }

    public static void lancerFinPartie(StatistiquesPartie stats, Partie model, HomePageView homeView) {
        FinPartieView finView = new FinPartieView(SceneManager.getStage(), stats, homeView.getBackground());
        new FinPartieController(finView, model, homeView, stats);
        SceneManager.show(finView.getScene(), "End Game");
    }
}