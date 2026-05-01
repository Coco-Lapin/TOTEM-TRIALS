package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.models.StatistiquesJoueur;
import com.totemtrials.totemtrials.models.StatistiquesPartie;
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

    private final Scene scene;
    private final VBox mainContent;
    private final StackPane statsOverlay;

    private final ImageView btnRejouer;
    private final ImageView btnStats;
    private final ImageView btnBack;
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
        btnBack = ViewUtils.createCroppedImageView(stage, IMG + "buttons/BackButton.png",      0.18);

        btnStats.getStyleClass().add("btn-rejouer");
        btnStats.setStyle("-fx-background-color: #D35400; -fx-text-fill: white; -fx-cursor: hand;");
        btnRejouer.setStyle("-fx-cursor: hand;");
        btnStats  .setStyle("-fx-cursor: hand;");
        btnBack.setStyle("-fx-cursor: hand;");

        btnBack.getStyleClass().add("btn-back");

        HBox btnBox = new HBox(25, btnRejouer, btnStats, btnBack);
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

        var css = FinPartieView.class.getResource("/styleSheet/finpartie.css");
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

        // box : taille explicite depuis stage — source de vérité unique
        // bgStats et content suivent box, pas l'inverse → pas de feedback loop
        StackPane box = new StackPane();
        box.prefWidthProperty().bind(stage.widthProperty().multiply(0.60)); //largeur du stage
        box.prefHeightProperty().bind(stage.heightProperty().multiply(0.82));  //hauteur du stage
        box.setMaxWidth(Region.USE_PREF_SIZE);
        box.setMaxHeight(Region.USE_PREF_SIZE);

        // bgStats bindé directement sur stage — même pattern que les valeurs du tableau
        ImageView bgStats = new ImageView(loadOrFallback("Images/BackGroundStatistique.png", null));
        bgStats.setPreserveRatio(false);
        bgStats.fitWidthProperty().bind(stage.widthProperty().multiply(0.60));
        bgStats.fitHeightProperty().bind(stage.heightProperty().multiply(0.82));

        // Titre image
        ImageView titre = ViewUtils.createCroppedImageView(stage, IMG + "statistique.png", 0.38);

        HBox global = new HBox(
                60,
                createMetric("TIME", stats.getDureeFormatee()),
                createMetric("ROUNDS", String.valueOf(stats.getTotalTours()))
        );
        global.setAlignment(Pos.CENTER);

        VBox playerList = buildPlayerList(stats.getClassement(), stage);

        btnFermerStats = ViewUtils.createCroppedImageView(stage, IMG + "buttons/BackButton.png", 0.10);
        btnFermerStats.setStyle("-fx-cursor: hand;");

        // content : remplit box entièrement (maxWidth/Height MAX_VALUE)
        // StackPane le redimensionne → Pos.CENTER centre les enfants verticalement
        VBox content = new VBox(12, titre, global, playerList, btnFermerStats);
        content.setAlignment(Pos.CENTER);
        content.paddingProperty().bind(
                stage.widthProperty().map(w -> new Insets(w.doubleValue() * 0.025)));
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMaxHeight(Double.MAX_VALUE);

        box.getChildren().addAll(bgStats, content);

        return new StackPane(dim, box);
    }

    private VBox buildPlayerList(StatistiquesJoueur[] classement, Stage stage) {
        String[] medals = {"🥇", "🥈", "🥉", "4.", "5.", "6."};

        // Toutes les largeurs de colonnes bindées sur stage — responsive
        var colMedal     = stage.widthProperty().multiply(0.040);
        var colToken     = stage.widthProperty().multiply(0.030);
        var colNom       = stage.widthProperty().multiply(0.070);
        var colGap       = stage.widthProperty().multiply(0.004);
        var colBonnes    = stage.widthProperty().multiply(0.070);
        var colMauvaises = stage.widthProperty().multiply(0.070);
        var colPct       = stage.widthProperty().multiply(0.070);
        var spacing      = stage.widthProperty().multiply(0.004);

        // En-têtes — police adaptive, PAS de maxWidth → texte visible même si long
        Label hMedal = new Label("POSITION");
        hMedal.styleProperty().bind(stage.widthProperty().map(w ->
                "-fx-text-fill: #4A3000; -fx-font-size: " +
                Math.max(6, (int)(w.doubleValue() * 0.007)) + "px; -fx-font-weight: bold;"));
        hMedal.prefWidthProperty().bind(colMedal);

        Label hToken = new Label("");
        hToken.prefWidthProperty().bind(colToken);

        Label hNom = new Label("PLAYER");
        hNom.styleProperty().bind(stage.widthProperty().map(w ->
                "-fx-text-fill: #4A3000; -fx-font-size: " +
                Math.max(6, (int)(w.doubleValue() * 0.007)) + "px; -fx-font-weight: bold;"));
        hNom.prefWidthProperty().bind(colNom);

        Region hGap = new Region();
        hGap.minWidthProperty().bind(colGap);

        Label hBonnes = new Label("CORRECT ANSWER");
        hBonnes.styleProperty().bind(stage.widthProperty().map(w ->
                "-fx-text-fill: #4A3000; -fx-font-size: " +
                Math.max(6, (int)(w.doubleValue() * 0.007)) + "px; -fx-font-weight: bold;"));
        hBonnes.prefWidthProperty().bind(colBonnes);

        Label hMauvaises = new Label("WRONG ANSWER");
        hMauvaises.styleProperty().bind(stage.widthProperty().map(w ->
                "-fx-text-fill: #4A3000; -fx-font-size: " +
                Math.max(6, (int)(w.doubleValue() * 0.007)) + "px; -fx-font-weight: bold;"));
        hMauvaises.prefWidthProperty().bind(colMauvaises);

        Label hPct = new Label("ACCURACY RATE");
        hPct.styleProperty().bind(stage.widthProperty().map(w ->
                "-fx-text-fill: #4A3000; -fx-font-size: " +
                Math.max(6, (int)(w.doubleValue() * 0.007)) + "px; -fx-font-weight: bold;"));
        hPct.prefWidthProperty().bind(colPct);

        HBox header = new HBox();
        header.spacingProperty().bind(spacing);
        header.getChildren().addAll(hMedal, hToken, hNom, hGap, hBonnes, hMauvaises, hPct);
        header.setAlignment(Pos.CENTER_LEFT);
        header.styleProperty().bind(stage.widthProperty().map(w -> {
            int p = (int)(w.doubleValue() * 0.008);
            return "-fx-padding: 4 " + p + " 6 " + p + "; " +
                   "-fx-border-color: #4A3000; -fx-border-width: 0 0 1 0;";
        }));

        VBox list = new VBox(4);
        for (int i = 0; i < classement.length; i++) {
            StatistiquesJoueur sj = classement[i];

            Label medal = new Label(i < medals.length ? medals[i] : (i + 1) + ".");
            medal.styleProperty().bind(stage.widthProperty().map(w ->
                    "-fx-font-size: " + Math.max(10, (int)(w.doubleValue() * 0.013)) + "px;"));
            medal.prefWidthProperty().bind(colMedal);
            medal.maxWidthProperty().bind(colMedal);

            ImageView token = new ImageView();
            token.fitWidthProperty().bind(colToken);
            token.setPreserveRatio(true);
            if (sj.getJoueur().getJeton() != null) {
                InputStream is = FinPartieView.class.getResourceAsStream(
                        "/" + sj.getJoueur().getJeton().getImagePath());
                if (is != null) token.setImage(new Image(is));
            }
            if (sj.getPosition() == 1) token.setEffect(new DropShadow(12, Color.GOLD));

            Label nom = new Label(sj.getJoueur().getNom());
            nom.styleProperty().bind(stage.widthProperty().map(w ->
                    "-fx-text-fill: #2A1500; -fx-font-size: " +
                    Math.max(10, (int)(w.doubleValue() * 0.011)) + "px; -fx-font-weight: bold;")); //changer la valeur derriere (w.doubleValue() pour modifier la taille de la police et celle apres Math.max(
            nom.prefWidthProperty().bind(colNom);
            nom.maxWidthProperty().bind(colNom);

            Region gap = new Region();
            gap.minWidthProperty().bind(colGap);

            Label bonnes = new Label("✓ " + sj.getBonnesReponses());
            bonnes.styleProperty().bind(stage.widthProperty().map(w ->
                    "-fx-text-fill: #2D6B00; -fx-font-size: " +
                    Math.max(9, (int)(w.doubleValue() * 0.010)) + "px; -fx-font-weight: bold;"));
            bonnes.prefWidthProperty().bind(colBonnes);
            bonnes.maxWidthProperty().bind(colBonnes);

            Label mauvaises = new Label("✗ " + sj.getMauvaisesReponses());
            mauvaises.styleProperty().bind(stage.widthProperty().map(w ->
                    "-fx-text-fill: #8B0000; -fx-font-size: " +
                    Math.max(9, (int)(w.doubleValue() * 0.010)) + "px; -fx-font-weight: bold;"));
            mauvaises.prefWidthProperty().bind(colMauvaises);
            mauvaises.maxWidthProperty().bind(colMauvaises);

            int pctVal = sj.getPourcentageReussite();
            String pctColor = pctVal >= 60 ? "#2D6B00" : pctVal >= 30 ? "#8B6000" : "#8B0000";
            Label pct = new Label(pctVal + "%");
            pct.styleProperty().bind(stage.widthProperty().map(w ->
                    "-fx-text-fill: " + pctColor + "; -fx-font-size: " +
                    Math.max(9, (int)(w.doubleValue() * 0.010)) + "px; -fx-font-weight: bold;"));
            pct.prefWidthProperty().bind(colPct);
            pct.maxWidthProperty().bind(colPct);

            HBox row = new HBox();
            row.spacingProperty().bind(spacing);
            row.getChildren().addAll(medal, token, nom, gap, bonnes, mauvaises, pct);
            row.setAlignment(Pos.CENTER_LEFT);
            row.styleProperty().bind(stage.widthProperty().map(w -> {
                int pv = (int)(w.doubleValue() * 0.005);
                int ph = (int)(w.doubleValue() * 0.009);
                return "-fx-padding: " + pv + " " + ph + " " + pv + " " + ph + ";";
            }));

            list.getChildren().add(row);
        }

        VBox wrapper = new VBox(0, header, list);
        // Contrainte max = disponible dans box (stage*0.60) moins padding content (stage*0.05)
        wrapper.maxWidthProperty().bind(stage.widthProperty().multiply(0.40));
        return wrapper;
    }

    private VBox createMetric(String label, String value) {

        //value to adjust if you need to change the text size for the statistique table title
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #4A3000; -fx-font-size: 12px; -fx-font-weight: bold;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: #3B1E00; -fx-font-size: 20px; -fx-font-weight: bold; " +
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

<<<<<<< HEAD
    private ImageView buildTokenView(StatistiquesJoueur sj) {

        if (sj.getJoueur().getJeton() == null) {
            System.err.println("Attention : Le joueur " + sj.getJoueur().getNom() + " n'a pas de jeton !");
            return new ImageView(); // Retourne une image vide au lieu de planter
        }
        InputStream is = FinPartieView.class.getResourceAsStream("/" + sj.getJoueur().getJeton().getImagePath());
        ImageView iv = new ImageView(is != null ? new Image(is) : null);
        iv.setFitWidth(90);
        iv.setPreserveRatio(true);
        if (sj.getPosition() == 1) {
            iv.setEffect(new DropShadow(25, Color.GOLD));
        }
        return iv;
    }

    public Scene getScene() { return scene; }
    public Button getBtnRejouer() { return btnRejouer; }
    public Button getBtnStats()   { return btnStats; }
    public Button getBtnQuitter() { return btnQuitter; }
=======
    public Scene     getScene()          { return scene; }
    public ImageView getBtnRejouer()     { return btnRejouer; }
    public ImageView getBtnStats()       { return btnStats; }
    public ImageView getBtnBack()     { return btnBack; }
    public ImageView getBtnFermerStats() { return btnFermerStats; }
>>>>>>> feat/menus
}