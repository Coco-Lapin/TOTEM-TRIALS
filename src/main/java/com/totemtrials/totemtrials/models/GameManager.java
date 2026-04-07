package com.totemtrials.totemtrials.models;


import com.totemtrials.totemtrials.controller.movementController;
import com.totemtrials.totemtrials.plateau.BoardGameController;
import com.totemtrials.totemtrials.plateau.Case;
import com.totemtrials.totemtrials.questions.GestionQuiz;
import com.totemtrials.totemtrials.questions.Question;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.List;

public class GameManager {
    private BoardGameController boardGameController;
    private movementController MC;
    private List<Case> listeCases;
    private int indexPositionActuelle = 0;
    private GestionQuiz nouveauQuiz;

    // Constructeur propre : on reçoit les outils déjà prêts
    public GameManager(BoardGameController bg, movementController mc, List<Case> cases) {
        this.boardGameController = bg;
        this.MC = mc;
        this.listeCases = cases;

    }

    public void demarrerPartie() {
        // On attend un peu pour laisser l'UI s'afficher
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            MC.initialiserPion();
            jouerUnTour(); // On lance le premier tour
        });
        pause.play();
    }

    public void jouerUnTour() {
        int distance = 1; // ou un dé

        // On passe un bloc lambda () -> { ... } comme argument "auFini"
        MC.deplacerPion(distance, () -> {

            System.out.println("Le pion s'est arrêté !");

            // C'est ici qu'on déclenche la suite de la logique
            this.analyserCaseArrivee(distance);

        });
    }


    public void analyserCaseArrivee(int distance) {
        Case c = this.boardGameController.getListeCases().get(distance);
        String tileTheme = c.getType();


        // 3. On demande au controller d'afficher la vue du quiz
        // (En supposant que tu as une méthode pour ajouter la VBox au StackPane)
        GestionQuiz nouveauQuiz = new GestionQuiz(tileTheme, (v) -> {

            this.boardGameController.fermerPopUpQuiz(v);

            });

        this.boardGameController.afficherPopUpQuiz(nouveauQuiz.getVue());

    }

}

