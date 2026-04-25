package com.totemtrials.totemtrials.controller;

import com.totemtrials.totemtrials.models.GameConfig;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import com.totemtrials.totemtrials.plateau.Case;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;

import com.totemtrials.totemtrials.plateau.BoardGameController;

import javafx.scene.layout.Pane;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class movementController {

    private String[] cheminImages = GameConfig.getInstance().getJetonsChoisis();

    private BoardGameController boardGame;
    private int indiceCaseActuelle = 0;

    @FXML private Pane plateauJeu; // Injecte le même Pane que dans BoardGameController
    // RETIRER LES INITIALISATIONS PLUS TARD
    private int[] indicesCasesActuelles = {0, 0, 0, 0};
    private Circle[] sprites = new Circle[4];

    // On définit 4 couleurs différentes pour les joueurs
    private Color[] couleursJoueurs = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};

    //méthodes à  implémenter plus tard
    public void setupPlayers(int param) {
        // 1. Initialisation des tableaux à la taille exacte demandée
        this.indicesCasesActuelles = new int[param];
        this.sprites = new Circle[param];
        // 2. Remplissage par défaut (optionnel car un int[] est à 0 par défaut)
        for (int i = 0; i < param; i++) {
            indicesCasesActuelles[i] = 0;
        }
    }
    //Initialisation du plateau et du Pane qui contient les cases ect ( NullPointerException)
    public void setBoardGame(BoardGameController bg) {this.boardGame = bg; }
    //-----------------------------------------------------------**
    public void setPlateauJeu(Pane p) {this.plateauJeu = p;}


    // 2. On initialise 4 pions au lieu d'un seul
    public void initialiserPions() {
        Case depart = boardGame.getListeCases().get(0);

        // A CHANGER ALLER DE I A SPRITES.LENGTHS
        for (int i = 0; i < GameConfig.getInstance().getNbJoueurs(); i++) {
            sprites[i] = new Circle(40); // J'ai un peu réduit le rayon (40) pour que les 4 rentrent mieux
            Image imgJeton = new Image(getClass().getResource(cheminImages[i]).toExternalForm());
            sprites[i].setFill(new ImagePattern(imgJeton));
            sprites[i].setStroke(Color.BLACK);
            sprites[i].setStrokeWidth(3);

            // PETITE ASTUCE : Décalage pour qu'ils ne se superposent pas tous au millimètre près
            // Joueur 0 (Bleu) : Haut-Gauche | Joueur 1 (Rouge) : Haut-Droit, etc.
            double posX = (i % 2 == 0) ? -20 : 20;
            double posY = (i < 2) ? -20 : 20;

            sprites[i].setTranslateX(depart.getCenterX() + posX);
            sprites[i].setTranslateY(depart.getCenterY() + posY);

            if (!plateauJeu.getChildren().contains(sprites[i])) {
                plateauJeu.getChildren().add(sprites[i]);
            }
        }
    }
    // 3. On ajoute l'argument "idJoueur" (de 0 à 3) pour savoir QUI bouge
    public void deplacerPion(int idJoueur, int nbCasesAAvancer, Runnable auFini) {
        List<Case> cases = boardGame.getListeCases();
        SequentialTransition trajetComplet = new SequentialTransition();

        // On recalcule le même décalage pour que le pion garde sa place relative sur la case
        double posX = (idJoueur % 2 == 0) ? -20 : 20;
        double posY = (idJoueur < 2) ? -20 : 20;

        if (nbCasesAAvancer >= 0) {
            for (int i = 0; i < nbCasesAAvancer; i++) {
                // On utilise le tableau d'indices avec l'idJoueur
                if (indicesCasesActuelles[idJoueur] + 1 < cases.size()) {
                    indicesCasesActuelles[idJoueur]++;
                    Case destination = cases.get(indicesCasesActuelles[idJoueur]);

                    // On anime le bon sprite
                    TranslateTransition saut = new TranslateTransition(Duration.millis(1000), sprites[idJoueur]);
                    saut.setToX(destination.getCenterX() + posX);
                    saut.setToY(destination.getCenterY() + posY);
                    trajetComplet.getChildren().add(saut);
                }
            }
        } else {
            int sautsEnArriere = Math.abs(nbCasesAAvancer);
            for (int i = 0; i < sautsEnArriere; i++) {
                if (indicesCasesActuelles[idJoueur] - 1 >= 0) {
                    indicesCasesActuelles[idJoueur]--;
                    Case destination = cases.get(indicesCasesActuelles[idJoueur]);

                    TranslateTransition saut = new TranslateTransition(Duration.millis(1000), sprites[idJoueur]);
                    saut.setToX(destination.getCenterX() + posX);
                    saut.setToY(destination.getCenterY() + posY);
                    trajetComplet.getChildren().add(saut);
                }
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
        int alea=rand.nextInt(10);
        System.out.println(alea);
        return alea;
    }
}
