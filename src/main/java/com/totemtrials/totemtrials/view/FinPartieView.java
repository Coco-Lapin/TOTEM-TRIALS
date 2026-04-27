package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.models.StatistiquesJoueur;
import com.totemtrials.totemtrials.models.StatistiquesPartie;
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

    // --- VALEURS DE BASE POUR L'ALIGNEMENT ---
    private static final int OFFSET_OR     = 370;
    private static final int OFFSET_ARGENT = 310;
    private static final int OFFSET_BRONZE = 280;
    private static final int LARGEUR_COLONNE = 200;

    public FinPartieView(Stage stage, StatistiquesPartie stats, Image fallbackBackground) {

        // 1. CHARGEMENT DU FOND (Podium)
        InputStream isPodium = FinPartieView.class.getResourceAsStream("/com/totemtrials/totemtrials/Images/fond-podium.jpg");
        ImageView bg = new ImageView((isPodium != null) ? new Image(isPodium) : fallbackBackground);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        // 2. CONTENU DU PODIUM
        Label titre = new Label("TOTEM TRIALS");
        titre.getStyleClass().add("titre-fin");

        HBox podium = buildPodiumPositions(stage, stats.getClassement());
        podium.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setVgrow(podium, Priority.ALWAYS);

        btnRejouer = new Button("⟳  REJOUER");
        btnRejouer.getStyleClass().add("btn-rejouer");

        btnStats = new Button("📊 VOIR STATS");
        btnStats.getStyleClass().add("btn-rejouer");
        // Style orange chaud pour le bouton stats
        btnStats.setStyle("-fx-background-color: #D35400; -fx-text-fill: white; -fx-cursor: hand;");

        btnQuitter = new Button("✕  QUITTER");
        btnQuitter.getStyleClass().add("btn-quitter");

        HBox btnBox = new HBox(25, btnRejouer, btnStats, btnQuitter);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(0, 0, 50, 0));

        mainContent = new VBox(20, titre, podium, btnBox);
        mainContent.setAlignment(Pos.CENTER);

        // 3. FENÊTRE POPUP DES STATS (Overlay)
        // On utilise 'stage' pour lier la taille et éviter le bug du root=null
        statsOverlay = createStatsPopup(stage, stats);
        statsOverlay.setVisible(false);
        statsOverlay.setOpacity(0);

        // 4. ASSEMBLAGE DANS LE STACKPANE
        root = new StackPane(bg, mainContent, statsOverlay);
        scene = new Scene(root, 1100, 750);

        var css = FinPartieView.class.getResource("/styleSheet/finpartie.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());
    }

    private StackPane createStatsPopup(Stage stage, StatistiquesPartie stats) {
        // Voile noir transparent
        Rectangle dim = new Rectangle();
        dim.widthProperty().bind(stage.widthProperty());
        dim.heightProperty().bind(stage.heightProperty());
        dim.setFill(Color.web("#000000", 0.85));

        // Boîte de dialogue des stats
        VBox box = new VBox(20);
        box.setAlignment(Pos.TOP_CENTER);
        box.setMaxSize(650, 550);
        box.setPadding(new Insets(35));
        box.setStyle("-fx-background-color: #1A1A1A; " +
                "-fx-border-color: #D35400; " +
                "-fx-border-width: 3; " +
                "-fx-background-radius: 20; " +
                "-fx-border-radius: 20;");
        box.setEffect(new DropShadow(30, Color.BLACK));

        Label t = new Label("STATISTIQUES DE LA PARTIE");
        t.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #D35400;");

        // Bloc des scores globaux
        HBox global = new HBox(60,
                createMetric("TEMPS", stats.getDureeFormatee()),
                createMetric("TOURS", String.valueOf(stats.getTotalTours()))
        );
        global.setAlignment(Pos.CENTER);

        // Liste des scores par joueur
        VBox list = new VBox(8);
        for (StatistiquesJoueur sj : stats.getClassement()) {
            HBox row = new HBox(20,
                    new Label("#" + sj.getPosition()),
                    new Label(sj.getJoueur().getNom()),
                    new Label(sj.getNombreTours() + " tours")
            );
            row.setStyle("-fx-background-color: #262626; -fx-padding: 12; -fx-background-radius: 8;");
            row.getChildren().get(0).setStyle("-fx-text-fill: #D35400; -fx-font-weight: bold; -fx-pref-width: 40;");
            row.getChildren().get(1).setStyle("-fx-text-fill: white; -fx-pref-width: 250; -fx-font-size: 16px;");
            row.getChildren().get(2).setStyle("-fx-text-fill: #95A5A6; -fx-font-size: 16px;");
            list.getChildren().add(row);
        }

        Button btnFermer = new Button("FERMER");
        btnFermer.getStyleClass().add("btn-quitter");
        btnFermer.setStyle("-fx-min-width: 200; -fx-background-radius: 30;");
        btnFermer.setOnAction(e -> toggleStats(false));

        box.getChildren().addAll(t, global, list, new Region(), btnFermer);
        VBox.setVgrow(list, Priority.ALWAYS);

        return new StackPane(dim, box);
    }

    private VBox createMetric(String label, String value) {
        Label l = new Label(label); l.setStyle("-fx-text-fill: #95A5A6; -fx-font-size: 11px;");
        Label v = new Label(value); v.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: bold;");
        VBox b = new VBox(l, v);
        b.setAlignment(Pos.CENTER);
        return b;
    }

    // Gère l'affichage/masquage avec animation et flou
    public void toggleStats(boolean show) {
        if (show) {
            statsOverlay.setVisible(true);
            mainContent.setEffect(new BoxBlur(8, 8, 3));
        }

        FadeTransition ft = new FadeTransition(Duration.millis(300), statsOverlay);
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

    private HBox buildPodiumPositions(Stage stage, StatistiquesJoueur[] classement) {
        int[] ordre = {1, 0, 2}; // Argent, Or, Bronze
        int[] offsets = {OFFSET_ARGENT, OFFSET_OR, OFFSET_BRONZE};
        String[] medailles = {"🥈", "🥇", "🥉"};
        HBox row = new HBox(20);
        row.setAlignment(Pos.BOTTOM_CENTER);

        for (int i = 0; i < 3; i++) {
            int idx = ordre[i];
            if (idx < classement.length) {
                StatistiquesJoueur sj = classement[idx];
                VBox player = new VBox(2);
                player.setAlignment(Pos.CENTER);

                ImageView token = buildTokenView(sj);
                Label m = new Label(medailles[i]); m.setStyle("-fx-font-size: 30px;");
                Label n = new Label(sj.getJoueur().getNom()); n.getStyleClass().add("nom-podium");

                player.getChildren().addAll(token, m, n);

                Region spacer = new Region();
                spacer.setMinHeight(offsets[i]);

                VBox col = new VBox(player, spacer);
                col.setAlignment(Pos.BOTTOM_CENTER);
                col.setPrefWidth(LARGEUR_COLONNE);
                row.getChildren().add(col);
            }
        }
        return row;
    }

    private ImageView buildTokenView(StatistiquesJoueur sj) {
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
}