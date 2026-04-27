package com.totemtrials.totemtrials.models;

import com.totemtrials.totemtrials.controller.movementController;
import com.totemtrials.totemtrials.plateau.BoardGameController;
import com.totemtrials.totemtrials.questions.GestionQuiz;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane; // <-- Import important
import javafx.scene.layout.VBox;

import java.util.Random;

public class Shortcut {
    private final int movementLose = -3;
    private final int movementWin = 7;

    // 1. On déclare view comme un StackPane
    private StackPane view;

    private BoardGameController boardGameController;
    private GestionQuiz gestionQuiz;
    private movementController MC;
    private GameManager gameManager;

    public Shortcut(BoardGameController bc, GameManager gm) {
        this.boardGameController = bc;
        this.gameManager = gm;
        // 2. On initialise le StackPane
        view = new StackPane();
    }

    public int getMovementLose() { return movementLose; }
    public int getMovementWin() { return movementWin; }

    // 3. La méthode renvoie bien un StackPane
    public StackPane displayBox() {
        this.view.getChildren().clear();

        // On utilise une VBox interne juste pour la mise en page (haut/bas)
        VBox contenu = new VBox(20);
        contenu.setAlignment(Pos.CENTER);
        contenu.setStyle("-fx-background-color: #F5DEB3; -fx-padding: 30; -fx-background-radius: 10; -fx-border-color: #5C3A1E; -fx-border-width: 3; -fx-border-radius: 10;");

        Label lbl = new Label("Do you want to use this shortcut ?");
        lbl.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #5C3A1E;");
        lbl.setAlignment(Pos.CENTER);

        HBox btnContainer = new HBox(20);
        btnContainer.setAlignment(Pos.CENTER);

        Button btnYes = new Button("Yes");
        Button btnNo = new Button("No");
        String styleBtn = "-fx-font-size: 15; -fx-min-width: 80; -fx-background-color: #5C3A1E; -fx-text-fill: white; -fx-cursor: hand;";
        btnYes.setStyle(styleBtn);
        btnNo.setStyle(styleBtn);

        btnContainer.getChildren().addAll(btnYes, btnNo);
        contenu.getChildren().addAll(lbl, btnContainer);

        // 4. On ajoute la VBox dans notre StackPane principal
        this.view.getChildren().add(contenu);
        this.view.setMaxSize(400, 200); // Taille de la pop-up de choix

        // --- Actions ---

        btnYes.setOnAction(e -> {
            // view est un StackPane, ça marche parfaitement avec ton BoardGameController !
            this.boardGameController.fermerPopUpQuiz(view);

            GestionQuiz nouveauQuiz = setupQuizHOP();
            this.boardGameController.afficherPopUpQuiz(nouveauQuiz.getVue());

        });

        btnNo.setOnAction(e -> {
            // view est un StackPane
            this.boardGameController.fermerPopUpQuiz(view);
            this.gameManager.EndingHop(false, false);
        });

        return view;
    }

    public GestionQuiz setupQuizHOP() {
        String tileTheme = "";
        Random rand = new Random();
        int alea = rand.nextInt(1,5);

        switch (alea) {
            case 1 -> tileTheme="entertainment";
            case 2 -> tileTheme="Tourism";
            case 3 -> tileTheme="Informatics";
            case 4 -> tileTheme="Mystery (Jumanji)";
        }

        GestionQuiz quiz = new GestionQuiz(tileTheme,4, boardGameController.getZoneCentrale(),"");

        quiz.setOnFinish(q -> {
            // q.getVue() renvoie un StackPane
            this.boardGameController.fermerPopUpQuiz(q.getVue());

            boolean estVictorieux = q.isCorrecte();
            this.gameManager.EndingHop(true, estVictorieux);
        });

        return quiz;
    }
}