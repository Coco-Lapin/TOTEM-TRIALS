package com.totemtrials.totemtrials.models;

import com.totemtrials.totemtrials.controller.movementController;
import com.totemtrials.totemtrials.plateau.BoardGameController;
import com.totemtrials.totemtrials.plateau.Case;
import com.totemtrials.totemtrials.questions.GestionQuiz;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.List;

public class GameManager {
    private BoardGameController boardGameController;
    private movementController MC;
    private List<Case> listeCases;

    // 1. Nouvelles variables pour le multijoueur
    private int[] positionsJoueurs = {0, 0, 0, 0}; // La position exacte de chaque joueur
    private int joueurActuel = 0; // 0 = Bleu, 1 = Rouge, 2 = Vert, 3 = Jaune

    public GameManager(BoardGameController bg, movementController mc, List<Case> cases) {
        this.boardGameController = bg;
        this.MC = mc;
        this.listeCases = cases;
    }

    public void demarrerPartie() {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            MC.initialiserPions(); // Fait apparaître les 4 pions
            jouerUnTour(); // Lance le tour du Joueur 0
        });
        pause.play();
    }

    public void jouerUnTour() {
        System.out.println("--- C'EST AU TOUR DU JOUEUR " + joueurActuel + " ---");

        // Ici, tu pourras brancher ton lancer de dé (ex: MC.nbCasesAParcourir())
        int distance = 2;

        // 2. On met à jour la position mathématique du joueur actuel
        positionsJoueurs[joueurActuel] += distance;

        // Sécurité : on empêche le pion d'aller plus loin que la case FINISH
        if (positionsJoueurs[joueurActuel] >= listeCases.size()) {
            positionsJoueurs[joueurActuel] = listeCases.size() - 1;
        }

        // 3. On déplace le bon pion, et une fois arrêté, on analyse la case
        MC.deplacerPion(joueurActuel, distance, () -> {
            System.out.println("Le pion du joueur " + joueurActuel + " s'est arrêté sur la case n°" + positionsJoueurs[joueurActuel]);
            this.analyserCaseArrivee(positionsJoueurs[joueurActuel]);
        });
    }

    public void analyserCaseArrivee(int positionActuelle) {
        // Petite optimisation : tu as déjà "listeCases" dans la classe, pas besoin de repasser par le controller
        Case c = this.listeCases.get(positionActuelle);
        String tileTheme = c.getType();

        if(tileTheme.equals("VERSUS") || tileTheme.equals("HOP")) {
            tileTheme = "Mystery (Jumanji)";
        }

        GestionQuiz nouveauQuiz = setupQuiz(tileTheme);
        this.boardGameController.afficherPopUpQuiz(nouveauQuiz.getVue());
    }

    private GestionQuiz setupQuiz(String tileTheme) {
        // On passe zoneCentrale pour le binding adaptatif
        GestionQuiz quiz = new GestionQuiz(tileTheme, boardGameController.getZoneCentrale());

        quiz.setOnFinish(q -> {
            // On ferme la fenêtre d'abord
            this.boardGameController.fermerPopUpQuiz(q.getVue());

            if (q.isCorrecte()) {
                int steps = q.getNiveauChoisi();
                System.out.println("Bonne réponse ! Le joueur " + joueurActuel + " avance de " + steps + " cases bonus.");

                // On met à jour la position après le bonus
                positionsJoueurs[joueurActuel] += steps;
                if (positionsJoueurs[joueurActuel] >= listeCases.size()) {
                    positionsJoueurs[joueurActuel] = listeCases.size() - 1;
                }

                // On déplace le pion pour le bonus, puis on passe au joueur suivant
                MC.deplacerPion(joueurActuel, steps, () -> {
                    passerAuJoueurSuivant();
                });

            } else {
                System.out.println("Mauvaise réponse. Fin du tour pour le joueur " + joueurActuel);
                // Si faux, le pion ne bouge pas, on passe juste au suivant
                passerAuJoueurSuivant();
            }
        });

        return quiz;
    }

    // 4. La méthode qui gère le roulement des tours
    private void passerAuJoueurSuivant() {
        joueurActuel++;

        // Si on dépasse le joueur 3 (le 4ème joueur), on revient au joueur 0
        if (joueurActuel > 3) {
            joueurActuel = 0;
        }

        // On met une petite pause d'une seconde pour que les joueurs respirent avant le prochain tour
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> jouerUnTour());
        pause.play();
    }
}