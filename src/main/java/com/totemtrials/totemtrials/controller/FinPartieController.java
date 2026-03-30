package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.model.Partie;
import com.totemtrials.totemtrials.model.StatistiquesPartie;
import com.totemtrials.totemtrials.view.FinPartieView;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.application.Platform;

/**
 * Câble les boutons de la FinPartieView.
 * Rejouer → recrée une Partie et retourne au menu principal.
 * Quitter → Platform.exit().
 */
public class FinPartieController {

    public FinPartieController(FinPartieView view, Partie model, HomePageView homeView) {

        view.getBtnRejouer().setOnAction(_ -> {
            // Réinitialise le modèle
            model.initJoueurs(0); // reset

            // Retour au menu principal — la nouvelle partie démarrera depuis là
            SceneManager.show(homeView.getScene(), "Menu principal");
        });

        view.getBtnQuitter().setOnAction(_ -> Platform.exit());
    }

    // ────────────────────────────────────────────────────────────────
    //  FACTORY — point d'entrée unique pour déclencher la fin de partie
    // ────────────────────────────────────────────────────────────────
    /**
     * Appelé depuis le controller de jeu quand un joueur atteint le centre.
     *
     * @param stats     statistiques calculées par la partie
     * @param model     le modèle global
     * @param homeView  référence à la HomePageView pour le bouton "Rejouer"
     */
    public static void lancerFinPartie(StatistiquesPartie stats, Partie model, HomePageView homeView) {
        FinPartieView finView = new FinPartieView(
            SceneManager.getStage(),
            stats,
            homeView.getBackground()
        );
        new FinPartieController(finView, model, homeView);
        SceneManager.show(finView.getScene(), "Fin de partie");
    }
}
