// ═══════════════════════════════════════════════════════════════
//  EXEMPLE D'INTÉGRATION — à coller dans ton controller de jeu
//  quand un joueur atteint la case centrale et répond correctement
// ═══════════════════════════════════════════════════════════════

package com.totemtrials.totemtrials;

import com.totemtrials.totemtrials.controller.FinPartieController;
import com.totemtrials.totemtrials.models.Joueur;
import com.totemtrials.totemtrials.models.Partie;
import com.totemtrials.totemtrials.models.StatistiquesJoueur;
import com.totemtrials.totemtrials.models.StatistiquesPartie;
import com.totemtrials.totemtrials.view.HomePageView;

public class ExempleIntegration {

    /**
     * Simule la fin d'une partie à 3 joueurs.
     * Dans ton vrai controller, remplace par tes vraies instances.
     */
    public static void exemple(Partie partie, HomePageView homeView) {

        Joueur[] joueurs = partie.getJoueurs();
        StatistiquesJoueur[] stats = new StatistiquesJoueur[joueurs.length];

        // --- Remplir les stats (dans le vrai jeu ces champs sont incrémentés en cours de partie) ---
        for (int i = 0; i < joueurs.length; i++) {
            stats[i] = new StatistiquesJoueur(joueurs[i]);
            stats[i].setPosition(i + 1);  // le vainqueur a position=1
            // Exemple de valeurs fictives
            for (int t = 0; t < 8 - i; t++)  stats[i].incrementerTour();
            for (int b = 0; b < 5 - i; b++)  stats[i].ajouterBonneReponse();
            for (int m = 0; m < 2 + i; m++)  stats[i].ajouterMauvaiseReponse();
        }

        long dureeSecondes = 754; // remplace par System.currentTimeMillis() / 1000 - debutPartie
        StatistiquesPartie statsPartie = new StatistiquesPartie(stats, dureeSecondes);

        // --- Déclenche l'écran de fin ---
        FinPartieController.lancerFinPartie(statsPartie, partie, homeView);
    }
}
