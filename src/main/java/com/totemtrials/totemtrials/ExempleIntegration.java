// ═══════════════════════════════════════════════════════════════
//  INTEGRATION EXAMPLE — paste this into your game controller
//  when a player reaches the central space and answers correctly
// ═══════════════════════════════════════════════════════════════

package com.totemtrials.totemtrials;

import com.totemtrials.totemtrials.controller.FinPartieController;
import com.totemtrials.totemtrials.model.*;
import com.totemtrials.totemtrials.model.Joueur;
import com.totemtrials.totemtrials.view.HomePageView;

public class ExempleIntegration {

    /**
     * Simulates the end of a 3-player game.
     * In your real controller, replace with your real instances.
     */
    public static void exemple(Partie partie, HomePageView homeView) {

        Joueur[] joueurs = partie.getJoueurs();
        StatistiquesJoueur[] stats = new StatistiquesJoueur[joueurs.length];

        // --- Fill stats (in the real game these fields are incremented during the match) ---
        for (int i = 0; i < joueurs.length; i++) {
            stats[i] = new StatistiquesJoueur(joueurs[i]);
            stats[i].setPosition(i + 1);  // the winner has position=1

            // Example of dummy values
            for (int t = 0; t < 8 - i; t++)  stats[i].incrementerTour();
            for (int b = 0; b < 5 - i; b++)  stats[i].ajouterBonneReponse();
            for (int m = 0; m < 2 + i; m++)  stats[i].ajouterMauvaiseReponse();
        }

        long dureeSecondes = 754; // replace with System.currentTimeMillis() / 1000 - startTime
        StatistiquesPartie statsPartie = new StatistiquesPartie(stats, dureeSecondes);

        // --- Triggers the end game screen ---
        FinPartieController.lancerFinPartie(statsPartie, partie, homeView);
    }
}