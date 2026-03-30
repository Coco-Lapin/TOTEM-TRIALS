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


    //Initialisation du plateau et du Pane qui contient les cases ect ( NullPointerException)
    public void setBoardGame(BoardGameController bg) {this.boardGame = bg; }
    //-----------------------------------------------------------**
    public void setPlateauJeu(Pane p) {this.plateauJeu = p;}

    // À appeler une fois au début pour faire apparaître le pion
    public void initialiserPion() {
        // Place le pion sur la case départ
        this.sprite = new Circle(60);
        this.sprite.setFill(Color.BLUE);
        this.sprite.setStroke(Color.BLACK);
        this.sprite.setStrokeWidth(3); // Epaisseur des bords du cercle
        Case depart = boardGame.getListeCases().get(0);// on détermine la case de départ en prenant l'index 0 de la liste qui contient les cases
        sprite.setTranslateX(depart.getCenterX());// on positionne le cercle "sprite" sur le centre de la case de départ
        sprite.setTranslateY(depart.getCenterY());
        // 2. Sécurité : on ne l'ajoute que s'il n'est pas déjà sur le plateau
        if (!plateauJeu.getChildren().contains(sprite)) {
            plateauJeu.getChildren().add(sprite);
        }

    }

    public void deplacerPion(int nbCasesAAvancer,Runnable auFini) {
        // Objet boardGame ( la classe ) on y extrait la liste des Cases ( chemin )
        List<Case> cases = boardGame.getListeCases();
        // Permet de créer des transitions fluides adapté à nos besoins
        SequentialTransition trajetComplet = new SequentialTransition();
        if (nbCasesAAvancer >= 0) {
            // Boucle qui part de 0 jusqu'au nombre de cases à avancer
            for (int i = 0; i < nbCasesAAvancer; i++) {
                if (indiceCaseActuelle + 1 < cases.size()) {
                    indiceCaseActuelle++;
                    Case destination = cases.get(indiceCaseActuelle);

                    TranslateTransition saut = new TranslateTransition(Duration.millis(1000), sprite);
                    saut.setToX(destination.getCenterX());
                    saut.setToY(destination.getCenterY());
                    trajetComplet.getChildren().add(saut);
                }
            }
        } else {
            // Si c'est négatif, on convertit en distance positive avec Math.abs
            int sautsEnArriere = Math.abs(nbCasesAAvancer);

            for (int i = 0; i < sautsEnArriere; i++) {
                if (indiceCaseActuelle - 1 >= 0) {
                    indiceCaseActuelle--;
                    Case destination = cases.get(indiceCaseActuelle);

                    TranslateTransition saut = new TranslateTransition(Duration.millis(1000), sprite);
                    saut.setToX(destination.getCenterX());
                    saut.setToY(destination.getCenterY());
                    trajetComplet.getChildren().add(saut);
                }
                // Retirer le "else { indiceCaseActuelle = 0; }" ici car ça casse l'animation
            }
        }

        trajetComplet.setOnFinished(e -> {
            if (auFini != null) auFini.run();
        });

        trajetComplet.play();
    }

    // METHODE PROVISOIRE permet de simuler le potentiel déplacement d'un joueur après réponse
    public int nbCasesAParcourir(){
        // création d'un objet Random
        Random rand = new Random();
        int alea=rand.nextInt(-10,10);
        System.out.println(alea);
        return alea;
    }


}
