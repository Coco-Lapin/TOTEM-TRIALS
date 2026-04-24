package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.model.*;
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
            Jeton jeton = entry.getKey();
            ImageView iv = entry.getValue();

            iv.setOnMouseClicked(_ -> assignJeton(jeton, model, view, joueursView, homeView));
        }
    }

    private void assignJeton(Jeton jeton, Partie model, ChoixJetonsView view,
                             ChoixJoueursView joueursView, HomePageView homeView) {
        Joueur[] joueurs = model.getJoueurs();
        if (currentPlayerIndex >= joueurs.length) return;

        // On affecte le jeton choisi au joueur
        joueurs[currentPlayerIndex].setJeton(jeton);
        System.out.printf("[ChoixJetons] %s -> %s%n",
                joueurs[currentPlayerIndex].getNom(), jeton.getNom());

        currentPlayerIndex++;

        // VERIFICATION : Si tous les joueurs ont un jeton, on affiche le classement
        if (currentPlayerIndex >= joueurs.length) {
            System.out.println("[ChoixJetons] Fin de sélection. Affichage du classement...");

            // On crée des statistiques de test pour remplir le podium
            StatistiquesJoueur[] statsJoueurs = new StatistiquesJoueur[joueurs.length];
            for (int i = 0; i < joueurs.length; i++) {
                statsJoueurs[i] = new StatistiquesJoueur(joueurs[i]);
                // On simule un classement basé sur l'ordre de création (1er, 2ème, etc.)
                statsJoueurs[i].setPosition(i + 1);
                statsJoueurs[i].incrementerTour();
                statsJoueurs[i].ajouterBonneReponse();
            }

            // On crée l'objet global avec une durée factice de 5 minutes (300s)
            StatistiquesPartie statsPartie = new StatistiquesPartie(statsJoueurs, 300);

            // On utilise ton controller existant pour changer de scène
            FinPartieController.lancerFinPartie(statsPartie, model, homeView);
        }
    }
}