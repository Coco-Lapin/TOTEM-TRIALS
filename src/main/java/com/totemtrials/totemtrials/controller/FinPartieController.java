package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.model.Partie;
import com.totemtrials.totemtrials.model.StatistiquesPartie;
import com.totemtrials.totemtrials.view.FinPartieView;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.application.Platform;

public class FinPartieController {

    public FinPartieController(FinPartieView view, Partie model, HomePageView homeView, StatistiquesPartie stats) {

        view.getBtnRejouer().setOnAction(_ -> {
            model.initJoueurs(0);
            SceneManager.show(homeView.getScene(), "Menu principal");
        });

        // ACTION : Afficher la popup Overlay
        view.getBtnStats().setOnAction(_ -> {
            view.toggleStats(true);
        });

        view.getBtnQuitter().setOnAction(_ -> Platform.exit());
    }

    public static void lancerFinPartie(StatistiquesPartie stats, Partie model, HomePageView homeView) {
        FinPartieView finView = new FinPartieView(SceneManager.getStage(), stats, homeView.getBackground());
        new FinPartieController(finView, model, homeView, stats);
        SceneManager.show(finView.getScene(), "Fin de partie");
    }
}