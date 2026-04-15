package com.totemtrials.totemtrials.models;

import com.totemtrials.totemtrials.controller.movementController;
import com.totemtrials.totemtrials.plateau.BoardGameController;
import com.totemtrials.totemtrials.questions.GestionQuiz;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Random;

public class Shortcut {
    private final int movementLose=-3;
    private final int movementWin=7;
    private VBox view;
    private BoardGameController  boardGameController;
    private GestionQuiz gestionQuiz;
    private movementController MC;

    private GameManager gameManager;

    public Shortcut(BoardGameController bc,GameManager gm) {
        this.boardGameController = bc;
        this.gameManager = gm; // On stocke le GameManager
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
    }

    public int getMovementLose() {
        return movementLose;
    }
    public int getMovementWin() {
        return movementWin;
    }

    public VBox getView() { return view; }

    public VBox displayBox(){

        Label lbl = new Label("Do you want to use this shortcut ?");
        lbl.setStyle("-fx-font-size: 20");
        lbl.setAlignment(Pos.CENTER);

        HBox Btncontainer = new HBox(10);
        Btncontainer.setAlignment(Pos.CENTER);

        Button btnYes = new Button("Yes");
        Button btnNo = new Button("No");
        btnYes.setStyle("-fx-font-size: 15");
        btnNo.setStyle("-fx-font-size: 15");
        Btncontainer.getChildren().addAll(btnYes, btnNo);
        this.view.getChildren().add(lbl);
        this.view.getChildren().add(Btncontainer);

        btnYes.setOnAction(e -> {
            this.boardGameController.fermerPopUpQuiz(view);
            GestionQuiz nouveauQuiz = setupQuizHOP();
            this.boardGameController.afficherPopUpQuiz(nouveauQuiz.getVue());
        });

        btnNo.setOnAction(e -> {
            this.boardGameController.fermerPopUpQuiz(view);
            // Le joueur a dit Non : aJoue = false, victoire = false
            this.gameManager.terminerHop(false, false);

        });

        return view; // On retourne la VBox prête à être affichée
    }

    public GestionQuiz setupQuizHOP() {
        String tileTheme = "";
        Random rand = new Random();
        int alea = rand.nextInt(1,5);

        switch (alea) {
            case 1: tileTheme="entertainment";
            break;
            case 2: tileTheme="Tourism";
            break;
            case 3: tileTheme="Informatics";
            break;
            case 4: tileTheme="Mystery (Jumanji)";
            break;
        }

        GestionQuiz quiz = new GestionQuiz(tileTheme);

        quiz.setOnFinish(q -> {
            this.boardGameController.fermerPopUpQuiz(q.getVue());

            // Le joueur a joué, on envoie "true" pour aJoue, et le résultat du quiz pour la victoire
            boolean estVictorieux = q.isCorrecte();
            this.gameManager.terminerHop(true, estVictorieux);
        });

        return quiz;
    }
}
