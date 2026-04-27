package com.totemtrials.totemtrials.models;

import com.totemtrials.totemtrials.plateau.BoardGameController;
import com.totemtrials.totemtrials.questions.GestionQuiz;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Random;

public class Versus {
    private StackPane contenu;
    private StackPane zoneCentrale;
    private VBox box;
    private GestionQuiz  gestionQuiz;
    private GameConfig gameConfig;
    private BoardGameController  boardGameController;
    private GameManager gameManager;
    private static final double POPUP_W = 0.40;
    private static final double POPUP_H = 0.85;
    private static final double PARCHEMIN_W = POPUP_W * 0.60; // zone utile sans bordures pierre
    private static final double PARCHEMIN_H = POPUP_H * 0.70;
    private int idChallenger;
    private int idAdversaire;
    private boolean challengerCorrect;
    private boolean adversaireCorrect;

    private int VersusLevel = 4;
    public Versus (StackPane ZC,GameConfig gm) {
        this.zoneCentrale = ZC;
        this.gameConfig=gm;
        box = new VBox(20);
        contenu = new  StackPane();

        contenu.prefWidthProperty().bind(zoneCentrale.widthProperty().multiply(POPUP_W));
        contenu.prefHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H));
        contenu.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 0);");
        contenu.getChildren().addAll(box);// rajouter l'image plus tard

        // choisir le theme  et le niveau l'appel du quiz
    }
    public StackPane getContenu() {
        return contenu;
    }

    public void choosePlayer(){

            // 1. On nettoie la box au cas où
            box.getChildren().clear();
            box.setAlignment(Pos.CENTER);

            // 2. Titre et instruction
            Label titre = creerLabel("VERSUS", 0.08); // Gros titre
            Label instruction = creerLabel("Choose your opponent :", 0.05);
            box.getChildren().addAll(titre, instruction);

            // 3. Récupération des infos du GameManager
            int actualPlayer = gameManager.getJoueurActuel(); // On récupère l'index (0 à 3)
            String[] playerNames =GameConfig.getInstance().getNomsJoueurs();

            // 4. Création des boutons pour les autres joueurs
        for (int i = 0; i < GameConfig.getInstance().getNbJoueurs(); i++) {
                if (i == actualPlayer) continue; // On ne peut pas se défier soi-même

                int adversaireId = i;
                Button btnAdversaire = new Button("Player " + playerNames[i]);


                btnAdversaire.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-text-fill: white; -fx-cursor: hand;");
                btnAdversaire.prefWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W * 0.8));

                btnAdversaire.setOnAction(e -> {
                    this.idChallenger = gameManager.getJoueurActuel();
                    this.idAdversaire = adversaireId; // L'index du joueur cliqué
                    System.out.println("Challenge launched ! " + idChallenger + " VS " + idAdversaire);
                    // On ferme la sélection d'adversaire
                    boardGameController.fermerPopUpQuiz(contenu);
                    // On lance la séquence
                    lancerTourChallenger();
                });
                box.getChildren().add(btnAdversaire);
            }
            if (contenu.getChildren().size() == 1) {
                ImageView bgView = new ImageView(
                        new Image(getClass().getResourceAsStream("/images/questions/backgroundQuestions.png"))
                );
                bgView.fitWidthProperty().bind(zoneCentrale.widthProperty().multiply(POPUP_W));
                bgView.fitHeightProperty().bind(zoneCentrale.heightProperty().multiply(POPUP_H));
                bgView.setPreserveRatio(false);

                // On insère l'image à l'index 0 (en dessous de la box)
                contenu.getChildren().add(0, bgView);
            }
    }

    // Crée un Label bold dont la taille de police est bindée sur la largeur du parchemin
    private Label creerLabel(String texte, double facteur) {
        Label label = new Label(texte);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.maxWidthProperty().bind(zoneCentrale.widthProperty().multiply(PARCHEMIN_W));
        label.fontProperty().bind(
                Bindings.createObjectBinding(
                        () -> Font.font("System", FontWeight.BOLD,
                                zoneCentrale.getWidth() * PARCHEMIN_W * facteur),
                        zoneCentrale.widthProperty()
                )
        );
        return label;
    }


    public GestionQuiz setupQuizVersus(String playerName) {
        String tileTheme = "";
        Random rand = new Random();
        int alea = rand.nextInt(1,5);

        switch (alea) {
            case 1 -> tileTheme="entertainment";
            case 2 -> tileTheme="Tourism";
            case 3 -> tileTheme="Computing";
            case 4 -> tileTheme="Mystery (Jumanji)";
        }
        GestionQuiz quiz = new GestionQuiz(tileTheme,4, boardGameController.getZoneCentrale(),playerName);
        quiz.setOnFinish(q -> {
            gestionQuiz.preparerEtAfficherQuestion();
            this.boardGameController.fermerPopUpQuiz(q.getVue());
        });
        return quiz;
    }

    private void lancerTourChallenger() {
        GestionQuiz quizChallenger = setupQuizVersus("Challenger");
        quizChallenger.setOnFinish(q -> {
            // On sauvegarde la réponse du challenger
            this.challengerCorrect = q.isCorrecte();
            this.boardGameController.fermerPopUpQuiz(q.getVue());

            // On enchaîne directement avec le tour de l'adversaire
            lancerTourAdversaire();
        });

        this.boardGameController.afficherPopUpQuiz(quizChallenger.getVue());
    }
    private void lancerTourAdversaire() {

        GestionQuiz quizAdversaire = setupQuizVersus("Opponent");
        quizAdversaire.setOnFinish(q -> {
            // On sauvegarde la réponse du défenseur
            this.adversaireCorrect = q.isCorrecte();
            this.boardGameController.fermerPopUpQuiz(q.getVue());

            // Les deux ont joué, on résout le combat !
            resoudreVersus();
        });

        this.boardGameController.afficherPopUpQuiz(quizAdversaire.getVue());
    }

    public void setGameManager(GameManager gameManager) { this.gameManager = gameManager; }

    public void setBoardGameController(BoardGameController boardGameController) {this.boardGameController = boardGameController;}

    private void resoudreVersus() {
        // On délègue au GameManager la responsabilité de déplacer les pions
        gameManager.EndingVersus(idChallenger, idAdversaire, challengerCorrect, adversaireCorrect);
    }
}

