package com.totemtrials.totemtrials.controller;

import javafx.scene.paint.Color;
import com.totemtrials.totemtrials.plateau.Case;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;

import com.totemtrials.totemtrials.plateau.BoardGameController;

import javafx.scene.layout.Pane;

import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class movementController {

    private BoardGameController boardGame;
    private int indiceCaseActuelle = 0; // Remonté ici !

    @FXML private Pane plateauJeu; // Injecte le même Pane que dans BoardGameController
    private Circle sprite = new Circle(60); // Rayon de 25, pas besoin de X, Y ici



    public void setBoardGame(BoardGameController bg) {
        this.boardGame = bg;
    }

    // CETTE MÉTHODE EST ESSENTIELLE
    public void setPlateauJeu(Pane p) {
        this.plateauJeu = p;
    }

    // À appeler une fois au début pour faire apparaître le pion
    public void initialiserPion() {
        // Place le pion sur la case départ
        this.sprite = new Circle(60); // Création
        this.sprite.setFill(Color.BLUE); // Color.Blue avec un B majuscule !
        this.sprite.setStroke(Color.BLACK);
        this.sprite.setStrokeWidth(3);
        Case depart = boardGame.getListeCases().get(0);
        sprite.setTranslateX(depart.getCenterX());
        sprite.setTranslateY(depart.getCenterY());
        plateauJeu.getChildren().add(sprite); // Ajoute le pion SUR le plateau
    }

    public void deplacerPion(int nbCasesAAvancer) {
        List<Case> cases = boardGame.getListeCases();
        SequentialTransition trajetComplet = new SequentialTransition();

        for (int i = 0; i < nbCasesAAvancer; i++) {
            if (indiceCaseActuelle + 1 < cases.size()) {
                indiceCaseActuelle++; // On mémorise la nouvelle position globale
                Case destination = cases.get(indiceCaseActuelle);

                TranslateTransition saut = new TranslateTransition(Duration.millis(1000), sprite);
                saut.setToX(destination.getCenterX());
                saut.setToY(destination.getCenterY());

                trajetComplet.getChildren().add(saut);
            }
        }
        trajetComplet.play();
    }

    // METHODE PROVISOIRE permet de simuler le potentiel déplacement d'un joueur après réponse
    public int nbCasesAParcourir(){

        Random rand = new Random();

        return rand.nextInt(35);

    }
}
