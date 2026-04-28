package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.model.StatistiquesJoueur;
import com.totemtrials.totemtrials.model.StatistiquesPartie;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;

public class FinPartieView {

    private final Scene scene;
    private final StackPane root;
    private final VBox mainContent;
    private final StackPane statsOverlay;

    private final Button btnRejouer;
    private final Button btnStats;
    private final Button btnQuitter;

    // =========================================================
    // VERTICAL RATIOS
    // =========================================================
    private static final double RATIO_ARGENT = 0.38;
    private static final double RATIO_OR     = 0.48;
    private static final double RATIO_BRONZE = 0.33;

    // =========================================================
    // HORIZONTAL POSITIONS
    // =========================================================
    private static final double POS_X_ARGENT = 0.31;
    private static final double POS_X_OR     = 0.51;
    private static final double POS_X_BRONZE = 0.70;

    public FinPartieView(Stage stage, StatistiquesPartie stats, Image fallbackBackground) {

        InputStream isPodium = FinPartieView.class.getResourceAsStream(
                "/com/totemtrials/totemtrials/Images/fond-podium.jpg"
        );

        ImageView bg = new ImageView(
                (isPodium != null) ? new Image(isPodium) : fallbackBackground
        );

        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        Label titre = new Label("TOTEM TRIALS");
        titre.getStyleClass().add("titre-fin");

        StackPane podiumPane = buildPodiumPane(stage, stats.getClassement());
        VBox.setVgrow(podiumPane, Priority.ALWAYS);

        btnRejouer = new Button("⟳  PLAY AGAIN");
        btnRejouer.getStyleClass().add("btn-rejouer");

        btnStats = new Button("📊 VIEW STATS");
        btnStats.getStyleClass().add("btn-rejouer");
        btnStats.setStyle("-fx-background-color: #D35400; -fx-text-fill: white; -fx-cursor: hand;");

        btnQuitter = new Button("✕  QUIT");
        btnQuitter.getStyleClass().add("btn-quitter");

        HBox btnBox = new HBox(25, btnRejouer, btnStats, btnQuitter);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(0, 0, 50, 0));

        mainContent = new VBox(20, titre, podiumPane, btnBox);
        mainContent.setAlignment(Pos.CENTER);

        statsOverlay = createStatsPopup(stage, stats);
        statsOverlay.setVisible(false);
        statsOverlay.setOpacity(0);

        root = new StackPane(bg, mainContent, statsOverlay);
        scene = new Scene(root, 1100, 750);

        var css = FinPartieView.class.getResource(
                "/com/totemtrials/totemtrials/styles/finpartie.css"
        );

        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }

    private StackPane buildPodiumPane(Stage stage, StatistiquesJoueur[] classement) {

        int[] ordre = {1, 0, 2};

        double[] ratiosY = {
                RATIO_ARGENT,
                RATIO_OR,
                RATIO_BRONZE
        };

        double[] posX = {
                POS_X_ARGENT,
                POS_X_OR,
                POS_X_BRONZE
        };

        String[] medailles = {"🥈", "🥇", "🥉"};

        StackPane pane = new StackPane();

        for (int i = 0; i < 3; i++) {

            int idx = ordre[i];

            if (idx >= classement.length) {
                continue;
            }

            StatistiquesJoueur sj = classement[idx];

            ImageView token = buildTokenView(sj, stage);

            Label medaille = new Label(medailles[i]);
            medaille.setStyle("-fx-font-size: 30px;");

            Label nom = new Label(sj.getJoueur().getNom());
            nom.getStyleClass().add("nom-podium");

            VBox playerInfo = new VBox(5, token, medaille, nom);
            playerInfo.setAlignment(Pos.CENTER);

            Region spacer = new Region();
            spacer.minHeightProperty().bind(
                    stage.heightProperty().multiply(ratiosY[i])
            );

            VBox col = new VBox(playerInfo, spacer);
            col.setAlignment(Pos.BOTTOM_CENTER);

            col.prefWidthProperty().bind(
                    stage.widthProperty().multiply(0.18)
            );

            col.translateXProperty().bind(
                    stage.widthProperty().multiply(posX[i] - 0.5)
            );

            pane.getChildren().add(col);
        }

        return pane;
    }

    private ImageView buildTokenView(StatistiquesJoueur sj, Stage stage) {

        ImageView iv = new ImageView();

        iv.fitWidthProperty().bind(
                stage.widthProperty().multiply(0.09)
        );

        iv.setPreserveRatio(true);

        if (sj.getJoueur().getJeton() != null) {

            InputStream is = FinPartieView.class.getResourceAsStream(
                    "/" + sj.getJoueur().getJeton().getImagePath()
            );

            if (is != null) {
                iv.setImage(new Image(is));
            }
        }

        if (sj.getPosition() == 1) {
            iv.setEffect(new DropShadow(25, Color.GOLD));
        }

        return iv;
    }

    private StackPane createStatsPopup(Stage stage, StatistiquesPartie stats) {

        Rectangle dim = new Rectangle();

        dim.widthProperty().bind(stage.widthProperty());
        dim.heightProperty().bind(stage.heightProperty());
        dim.setFill(Color.web("#000000", 0.85));

        VBox box = new VBox(20);
        box.setAlignment(Pos.TOP_CENTER);
        box.setMaxSize(650, 550);
        box.setPadding(new Insets(35));

        box.setStyle(
                "-fx-background-color: #1A1A1A;" +
                        "-fx-border-color: #D35400;" +
                        "-fx-border-width: 3;" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;"
        );

        box.setEffect(new DropShadow(30, Color.BLACK));

        Label t = new Label("GAME STATISTICS");
        t.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #D35400;"
        );

        HBox global = new HBox(
                60,
                createMetric("TIME", stats.getDureeFormatee()),
                createMetric("ROUNDS", String.valueOf(stats.getTotalTours()))
        );

        global.setAlignment(Pos.CENTER);

        VBox list = new VBox(8);

        for (StatistiquesJoueur sj : stats.getClassement()) {

            HBox row = new HBox(
                    20,
                    new Label("#" + sj.getPosition()),
                    new Label(sj.getJoueur().getNom()),
                    new Label(sj.getNombreTours() + " ROUNDS")
            );

            row.setStyle(
                    "-fx-background-color: #262626;" +
                            "-fx-padding: 12;" +
                            "-fx-background-radius: 8;"
            );

            row.getChildren().get(0).setStyle(
                    "-fx-text-fill: #D35400;" +
                            "-fx-font-weight: bold;" +
                            "-fx-pref-width: 40;"
            );

            row.getChildren().get(1).setStyle(
                    "-fx-text-fill: white;" +
                            "-fx-pref-width: 250;" +
                            "-fx-font-size: 16px;"
            );

            row.getChildren().get(2).setStyle(
                    "-fx-text-fill: #95A5A6;" +
                            "-fx-font-size: 16px;"
            );

            list.getChildren().add(row);
        }

        Button btnFermer = new Button("FERMER");
        btnFermer.getStyleClass().add("btn-quitter");

        btnFermer.setStyle(
                "-fx-min-width: 200;" +
                        "-fx-background-radius: 30;"
        );

        btnFermer.setOnAction(e -> toggleStats(false));

        box.getChildren().addAll(t, global, list, new Region(), btnFermer);
        VBox.setVgrow(list, Priority.ALWAYS);

        return new StackPane(dim, box);
    }

    private VBox createMetric(String label, String value) {

        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #95A5A6; -fx-font-size: 11px;");

        Label v = new Label(value);
        v.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: bold;");

        VBox b = new VBox(l, v);
        b.setAlignment(Pos.CENTER);

        return b;
    }

    public void toggleStats(boolean show) {

        if (show) {
            statsOverlay.setVisible(true);
            mainContent.setEffect(new BoxBlur(8, 8, 3));
        }

        FadeTransition ft = new FadeTransition(
                Duration.millis(300),
                statsOverlay
        );

        ft.setFromValue(show ? 0 : 1);
        ft.setToValue(show ? 1 : 0);

        ft.setOnFinished(e -> {
            if (!show) {
                statsOverlay.setVisible(false);
                mainContent.setEffect(null);
            }
        });

        ft.play();
    }

    public Scene getScene() {
        return scene;
    }

    public Button getBtnRejouer() {
        return btnRejouer;
    }

    public Button getBtnStats() {
        return btnStats;
    }

    public Button getBtnQuitter() {
        return btnQuitter;
    }
}