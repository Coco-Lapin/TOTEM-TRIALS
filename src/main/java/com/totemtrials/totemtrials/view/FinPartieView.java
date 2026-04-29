package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.model.StatistiquesJoueur;
import com.totemtrials.totemtrials.model.StatistiquesPartie;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

    private static final String IMG = "com/totemtrials/totemtrials/Images/";

    private static final double RATIO_ARGENT = 0.38;
    private static final double RATIO_OR     = 0.48;
    private static final double RATIO_BRONZE = 0.33;

    private static final double POS_X_ARGENT = 0.31;
    private static final double POS_X_OR     = 0.51;
    private static final double POS_X_BRONZE = 0.70;

    private final Scene scene;
    private final VBox mainContent;
    private final StackPane statsOverlay;

    private final ImageView btnRejouer;
    private final ImageView btnStats;
    private final ImageView btnQuitter;
    private ImageView btnFermerStats;

    public FinPartieView(Stage stage, StatistiquesPartie stats, Image fallbackBackground) {

        // FOND PODIUM
        ImageView bg = new ImageView(loadOrFallback("Images/fond-podium.jpg", fallbackBackground));
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        // TITRE IMAGE
        ImageView titre = ViewUtils.createCroppedImageView(stage, IMG + "TitreLong.png", 0.55);

        // PODIUM
        StackPane podiumPane = buildPodiumPane(stage, stats.getClassement());
        VBox.setVgrow(podiumPane, Priority.ALWAYS);

        // BOUTONS IMAGE
        btnRejouer = ViewUtils.createCroppedImageView(stage, IMG + "buttons/PlayAgain.png", 0.25);
        btnStats   = ViewUtils.createCroppedImageView(stage, IMG + "buttons/ViewStats.png", 0.25);
        btnQuitter = ViewUtils.createCroppedImageView(stage, IMG + "buttons/Exit-sora.png",      0.18);

        btnRejouer.setStyle("-fx-cursor: hand;");
        btnStats  .setStyle("-fx-cursor: hand;");
        btnQuitter.setStyle("-fx-cursor: hand;");

        HBox btnBox = new HBox(btnRejouer, btnStats, btnQuitter);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.spacingProperty().bind(stage.widthProperty().multiply(0.04));
        btnBox.paddingProperty().bind(
                stage.heightProperty().map(h -> new Insets(0, 0, h.doubleValue() * 0.06, 0))
        );

        mainContent = new VBox(titre, podiumPane, btnBox);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.spacingProperty().bind(stage.heightProperty().multiply(0.02));

        // OVERLAY STATS
        statsOverlay = createStatsPopup(stage, stats);
        statsOverlay.setVisible(false);
        statsOverlay.setOpacity(0);

        StackPane root = new StackPane(bg, mainContent, statsOverlay);
        scene = new Scene(root, 1100, 750);

        var css = FinPartieView.class.getResource("/com/totemtrials/totemtrials/styles/finpartie.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    private StackPane buildPodiumPane(Stage stage, StatistiquesJoueur[] classement) {

        int[]    ordre    = {1, 0, 2};
        double[] ratiosY  = {RATIO_ARGENT, RATIO_OR, RATIO_BRONZE};
        double[] posX     = {POS_X_ARGENT, POS_X_OR, POS_X_BRONZE};
        String[] medailles = {"🥈", "🥇", "🥉"};

        StackPane pane = new StackPane();

        for (int i = 0; i < 3; i++) {
            int idx = ordre[i];
            if (idx >= classement.length) continue;

            StatistiquesJoueur sj = classement[idx];

            ImageView token = buildTokenView(sj, stage);

            Label medaille = new Label(medailles[i]);
            medaille.setStyle("-fx-font-size: 30px;");

            Label nom = new Label(sj.getJoueur().getNom());
            nom.getStyleClass().add("nom-podium");

            VBox playerInfo = new VBox(5, token, medaille, nom);
            playerInfo.setAlignment(Pos.CENTER);

            Region spacer = new Region();
            spacer.minHeightProperty().bind(stage.heightProperty().multiply(ratiosY[i]));

            VBox col = new VBox(playerInfo, spacer);
            col.setAlignment(Pos.BOTTOM_CENTER);
            col.prefWidthProperty().bind(stage.widthProperty().multiply(0.18));
            col.translateXProperty().bind(stage.widthProperty().multiply(posX[i] - 0.5));

            pane.getChildren().add(col);
        }
        return pane;
    }

    private ImageView buildTokenView(StatistiquesJoueur sj, Stage stage) {
        ImageView iv = new ImageView();
        iv.fitWidthProperty().bind(stage.widthProperty().multiply(0.09));
        iv.setPreserveRatio(true);

        if (sj.getJoueur().getJeton() != null) {
            InputStream is = FinPartieView.class.getResourceAsStream(
                    "/" + sj.getJoueur().getJeton().getImagePath());
            if (is != null) iv.setImage(new Image(is));
        }

        if (sj.getPosition() == 1) iv.setEffect(new DropShadow(25, Color.GOLD));
        return iv;
    }

    private StackPane createStatsPopup(Stage stage, StatistiquesPartie stats) {

        // Voile sombre plein écran
        Rectangle dim = new Rectangle();
        dim.widthProperty().bind(stage.widthProperty());
        dim.heightProperty().bind(stage.heightProperty());
        dim.setFill(Color.web("#000000", 0.78));

        // Fond image : taille bindée sur stage (pas sur box → pas de feedback loop)
        ImageView bgStats = new ImageView(loadOrFallback("Images/BackGroundStatistique.png", null));
        bgStats.setPreserveRatio(false);
        bgStats.fitWidthProperty().bind(stage.widthProperty().multiply(0.52));
        bgStats.setFitHeight(560);

        // Titre
        Label titre = new Label("STATISTIQUES");
        titre.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #FFD700; " +
                       "-fx-font-family: 'Georgia', serif;");

        // Stats globales
        HBox global = new HBox(60,
                createMetric("DURÉE",  stats.getDureeFormatee()),
                createMetric("TOURS",  String.valueOf(stats.getTotalTours()))
        );
        global.setAlignment(Pos.CENTER);

        // Tableau joueurs
        VBox playerList = buildPlayerList(stats.getClassement());

        // Bouton fermer
        btnFermerStats = ViewUtils.createCroppedImageView(stage, IMG + "buttons/Exit-sora.png", 0.14);
        btnFermerStats.setStyle("-fx-cursor: hand;");

        VBox content = new VBox(16, titre, global, playerList, btnFermerStats);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPadding(new Insets(30, 40, 30, 40));

        // Boite popup : bg image + contenu (taille max fixe)
        StackPane box = new StackPane(bgStats, content);
        box.maxWidthProperty().bind(stage.widthProperty().multiply(0.52));
        box.setMaxHeight(560);
        box.setStyle(
                "-fx-border-radius: 12; -fx-background-radius: 12; " +
                "-fx-effect: dropshadow(gaussian, #000000DD, 40, 0.5, 0, 6);");

        return new StackPane(dim, box);
    }

    private VBox buildPlayerList(StatistiquesJoueur[] classement) {
        String[] medals  = {"🥇", "🥈", "🥉", "4.", "5.", "6."};
        String[] bgColors = {"#3A280066", "#2A2A2A66", "#2A1A0A66"};

        VBox list = new VBox(6);
        for (int i = 0; i < classement.length; i++) {
            StatistiquesJoueur sj = classement[i];

            Label medal = new Label(i < medals.length ? medals[i] : (i + 1) + ".");
            medal.setStyle("-fx-font-size: 18px;");
            medal.setMinWidth(36);

            ImageView token = new ImageView();
            token.setFitWidth(32);
            token.setPreserveRatio(true);
            if (sj.getJoueur().getJeton() != null) {
                InputStream is = FinPartieView.class.getResourceAsStream(
                        "/" + sj.getJoueur().getJeton().getImagePath());
                if (is != null) token.setImage(new Image(is));
            }
            if (sj.getPosition() == 1) token.setEffect(new DropShadow(12, Color.GOLD));

            Label nom = new Label(sj.getJoueur().getNom());
            nom.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            nom.setMinWidth(140);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label tours = new Label(sj.getNombreTours() + " tours");
            tours.setStyle("-fx-text-fill: #C8D8A0; -fx-font-size: 13px;");

            int pctVal = sj.getPourcentageReussite();
            String pctColor = pctVal >= 60 ? "#7EC850" : pctVal >= 30 ? "#FFD700" : "#E05050";
            Label pct = new Label(pctVal + "%");
            pct.setStyle("-fx-text-fill: " + pctColor + "; -fx-font-size: 13px; -fx-font-weight: bold;");
            pct.setMinWidth(45);

            HBox row = new HBox(12, medal, token, nom, spacer, tours, pct);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setStyle("-fx-padding: 8 12 8 12;");

            list.getChildren().add(row);
        }
        return list;
    }

    private VBox createMetric(String label, String value) {
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #8DC84A; -fx-font-size: 11px; -fx-font-weight: bold;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 26px; -fx-font-weight: bold; " +
                   "-fx-font-family: 'Georgia', serif;");
        VBox b = new VBox(2, l, v);
        b.setAlignment(Pos.CENTER);
        return b;
    }

    public void toggleStats(boolean show) {
        if (show) {
            statsOverlay.setVisible(true);
            mainContent.setEffect(new BoxBlur(8, 8, 3));
        }
        FadeTransition ft = new FadeTransition(Duration.millis(300), statsOverlay);
        ft.setFromValue(show ? 0 : 1);
        ft.setToValue(show ? 1 : 0);
        ft.setOnFinished(_ -> {
            if (!show) {
                statsOverlay.setVisible(false);
                mainContent.setEffect(null);
            }
        });
        ft.play();
    }

    private static Image loadOrFallback(String relativePath, Image fallback) {
        InputStream is = FinPartieView.class.getResourceAsStream(
                "/com/totemtrials/totemtrials/" + relativePath);
        return (is != null) ? new Image(is) : fallback;
    }

    public Scene     getScene()          { return scene; }
    public ImageView getBtnRejouer()     { return btnRejouer; }
    public ImageView getBtnStats()       { return btnStats; }
    public ImageView getBtnQuitter()     { return btnQuitter; }
    public ImageView getBtnFermerStats() { return btnFermerStats; }
}