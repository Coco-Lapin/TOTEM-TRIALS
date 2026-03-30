package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.model.Jeton;
import com.totemtrials.totemtrials.model.Joueur;
import com.totemtrials.totemtrials.model.Partie;
import com.totemtrials.totemtrials.view.ChoixJetonsView;
import com.totemtrials.totemtrials.view.ChoixJoueursView;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.scene.image.ImageView;

import java.util.Map;

public class ChoixJetonsController {

    private int currentPlayerIndex = 0;

    public ChoixJetonsController(ChoixJetonsView view, Partie model,
                                  ChoixJoueursView joueursView, HomePageView homeView) {

        view.getBackButton().setOnAction(_ ->
            SceneManager.show(joueursView.getScene(), "Choix des joueurs")
        );

        Map<Jeton, ImageView> jetonViews = view.getJetonViews();

        for (Map.Entry<Jeton, ImageView> entry : jetonViews.entrySet()) {
            Jeton    jeton = entry.getKey();
            ImageView iv   = entry.getValue();

            iv.setOnMouseClicked(_ -> assignJeton(jeton, model, view, joueursView, homeView));
        }
    }

    /**
     * Affecte le jeton cliqué au joueur courant, puis passe au suivant.
     * Quand tous les joueurs ont leur jeton → lancer la partie (TODO).
     */
    private void assignJeton(Jeton jeton, Partie model, ChoixJetonsView view,
                              ChoixJoueursView joueursView, HomePageView homeView) {
        Joueur[] joueurs = model.getJoueurs();
        if (currentPlayerIndex >= joueurs.length) return;

        joueurs[currentPlayerIndex].setJeton(jeton);
        System.out.printf("[ChoixJetons] %s → %s%n",
                joueurs[currentPlayerIndex].getNom(), jeton.getNom());

        currentPlayerIndex++;

        if (currentPlayerIndex >= joueurs.length) {
            // TODO : lancer la scène de jeu
            System.out.println("[ChoixJetons] Tous les joueurs ont leur jeton. Démarrage...");
        }
    }
}
