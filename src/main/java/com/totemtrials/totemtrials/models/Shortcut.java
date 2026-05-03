package com.totemtrials.totemtrials.models;

import com.totemtrials.totemtrials.plateau.BoardGameController;
import com.totemtrials.totemtrials.questions.GestionQuiz;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.Random;

public class Shortcut {
    private final int movementLose = -3;
    private final int movementWin  = 7;

    private StackPane view;
    private BoardGameController boardGameController;
    private GestionQuiz gestionQuiz;
    private GameManager gameManager;

    private static final String BTN_NORMAL  =
            "-fx-background-color: #5C3A1E; -fx-text-fill: #F5DEB3; -fx-background-radius: 8; -fx-cursor: hand;";
    private static final String BTN_HOVER   =
            "-fx-background-color: #7a4e2a; -fx-text-fill: #FFE97A; -fx-background-radius: 8; -fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, #c8922a, 10, 0.5, 0, 0);";
    private static final String BTN_PRESSED =
            "-fx-background-color: #3d2410; -fx-text-fill: #F5DEB3; -fx-background-radius: 8; -fx-cursor: hand;";

    public Shortcut(BoardGameController bc, GameManager gm) {
        this.boardGameController = bc;
        this.gameManager = gm;
        view = new StackPane();
    }

    public int getMovementLose() { return movementLose; }
    public int getMovementWin()  { return movementWin; }

    private static final double POP_W     = 0.40;
    private static final double POP_H     = 0.55;
    private static final double CONTENT_W = POP_W * 0.58;

    public StackPane displayBox() {
        this.view.getChildren().clear();
        StackPane zc = boardGameController.getZoneCentrale();

        // Fond parchemin adaptatif
        ImageView bgView = new ImageView(
                new Image(getClass().getResourceAsStream("/images/questions/backgroundQuestions.png"))
        );
        bgView.fitWidthProperty().bind(zc.widthProperty().multiply(POP_W));
        bgView.fitHeightProperty().bind(zc.heightProperty().multiply(POP_H));
        bgView.setPreserveRatio(false);

        // Popup container — pref ET max bindés pour rester centré dans le backdrop
        view.prefWidthProperty().bind(zc.widthProperty().multiply(POP_W));
        view.prefHeightProperty().bind(zc.heightProperty().multiply(POP_H));
        view.maxWidthProperty().bind(zc.widthProperty().multiply(POP_W));
        view.maxHeightProperty().bind(zc.heightProperty().multiply(POP_H));
        view.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 20, 0, 0, 0);");

        // Fix 1 — titre joueur dominant
        String[] noms = GameConfig.getInstance().getNomsJoueurs();
        int idx = gameManager.getJoueurActuel();
        String nomJoueur = (idx < noms.length) ? noms[idx] : "Player";

        Label titre = new Label(nomJoueur + "'s turn");
        titre.setWrapText(true);
        titre.setAlignment(Pos.CENTER);
        titre.maxWidthProperty().bind(zc.widthProperty().multiply(CONTENT_W));
        titre.fontProperty().bind(
                Bindings.createObjectBinding(
                        () -> Font.font("Impact", FontWeight.BOLD, zc.getWidth() * CONTENT_W * 0.14),
                        zc.widthProperty()
                )
        );
        titre.setStyle("-fx-text-fill: #5C3A1E;");

        // Sous-titre question
        Label lbl = new Label("Use this shortcut?");
        lbl.setWrapText(true);
        lbl.setAlignment(Pos.CENTER);
        lbl.maxWidthProperty().bind(zc.widthProperty().multiply(CONTENT_W));
        lbl.fontProperty().bind(
                Bindings.createObjectBinding(
                        () -> Font.font("Impact", FontWeight.NORMAL, zc.getWidth() * CONTENT_W * 0.09),
                        zc.widthProperty()
                )
        );
        lbl.setStyle("-fx-text-fill: #7a4e2a;");

        Button btnYes = creerBouton("Yes", zc);
        Button btnNo  = creerBouton("No",  zc);

        HBox btnRow = new HBox(20, btnYes, btnNo);
        btnRow.setAlignment(Pos.CENTER);

        VBox contenu = new VBox(12, titre, lbl, btnRow);
        contenu.setAlignment(Pos.CENTER);
        contenu.prefWidthProperty().bind(zc.widthProperty().multiply(CONTENT_W));
        contenu.maxWidthProperty().bind(zc.widthProperty().multiply(CONTENT_W));
        contenu.prefHeightProperty().bind(zc.heightProperty().multiply(POP_H));

        view.getChildren().addAll(bgView, contenu);

        btnYes.setOnAction(e -> {
            boardGameController.fermerPopUpQuiz(view);
            GestionQuiz quiz = setupQuizHOP();
            boardGameController.afficherPopUpQuiz(quiz.getVue());
        });
        btnNo.setOnAction(e -> {
            boardGameController.fermerPopUpQuiz(view);
            gameManager.EndingHop(false, false);
        });

        return view;
    }

    private Button creerBouton(String texte, StackPane zc) {
        Button b = new Button(texte);
        b.setStyle(BTN_NORMAL);
        b.setMinWidth(80);

        b.fontProperty().bind(
                Bindings.createObjectBinding(
                        () -> Font.font("Impact", FontWeight.BOLD, zc.getWidth() * 0.018),
                        zc.widthProperty()
                )
        );

        b.setOnMouseEntered(e -> {
            b.setStyle(BTN_HOVER);
            ScaleTransition st = new ScaleTransition(Duration.millis(80), b);
            st.setToX(1.03); st.setToY(1.03);
            st.play();
        });
        b.setOnMouseExited(e -> {
            b.setStyle(BTN_NORMAL);
            ScaleTransition st = new ScaleTransition(Duration.millis(80), b);
            st.setToX(1.0); st.setToY(1.0);
            st.play();
        });
        b.setOnMousePressed(e -> {
            b.setStyle(BTN_PRESSED);
            b.setTranslateY(2);
        });
        b.setOnMouseReleased(e -> {
            b.setStyle(BTN_HOVER);
            b.setTranslateY(0);
        });

        return b;
    }

    public GestionQuiz setupQuizHOP() {
        String tileTheme = "";
        Random rand = new Random();
        switch (rand.nextInt(1, 5)) {
            case 1 -> tileTheme = "entertainment";
            case 2 -> tileTheme = "Tourism";
            case 3 -> tileTheme = "Computing";
            case 4 -> tileTheme = "Mystery";
        }

        GestionQuiz quiz = new GestionQuiz(tileTheme, 4, boardGameController.getZoneCentrale(), "", null);
        quiz.setOnFinish(q -> {
            boardGameController.fermerPopUpQuiz(q.getVue());
            gameManager.EndingHop(true, q.isCorrecte());
        });
        return quiz;
    }
}