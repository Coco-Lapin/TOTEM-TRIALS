package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.Partie;
import com.totemtrials.totemtrials.models.StatistiquesPartie;
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
            SceneManager.show(cjv.getScene(), "Chose your token");
        });

        view.getBtnStats().setOnMouseClicked(_ -> view.toggleStats(true));

        view.getBtnBack().setOnMouseClicked(_ -> SceneManager.show(SceneManager.getHomeScene(), "Main menu"));

        view.getBtnFermerStats().setOnMouseClicked(_ -> view.toggleStats(false));
    }

    public static void lancerFinPartie(StatistiquesPartie stats, Partie model, HomePageView homeView) {
        if (homeView == null) {
            System.err.println("Erreur : homeView is null in lancerFinPartie !");
            return;
        }

        // On récupère le background de l'accueil pour la vue de fin
        FinPartieView finView = new FinPartieView(SceneManager.getStage(), stats, homeView.getBackground());
        new FinPartieController(finView, model, homeView, stats);
        SceneManager.show(finView.getScene(), "End Game");
    }
}