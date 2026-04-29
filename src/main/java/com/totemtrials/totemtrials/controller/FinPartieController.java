package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.GameManager;
import com.totemtrials.totemtrials.models.Partie;
import com.totemtrials.totemtrials.models.StatistiquesPartie;
import com.totemtrials.totemtrials.view.FinPartieView;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.application.Platform;

public class FinPartieController {


    public FinPartieController(FinPartieView view, Partie model, HomePageView homeView, StatistiquesPartie stats) {


        view.getBtnRejouer().setOnAction(_ -> {
            model.initJoueurs(0);
            // On utilise la scène de homeView qui est maintenant garantie non-null
            SceneManager.show(homeView.getScene(), "Menu principal");
        });

        view.getBtnStats().setOnAction(_ -> view.toggleStats(true));
        view.getBtnQuitter().setOnAction(_ -> Platform.exit());
    }

    public static void lancerFinPartie(StatistiquesPartie stats, Partie model, HomePageView homeView) {
        if (homeView == null) {
            System.err.println("Erreur : homeView est null dans lancerFinPartie !");
            return;
        }

        // On récupère le background de l'accueil pour la vue de fin
        FinPartieView finView = new FinPartieView(SceneManager.getStage(), stats, homeView.getBackground());
        new FinPartieController(finView, model, homeView, stats);
        SceneManager.show(finView.getScene(), "Fin de partie");
    }
}
