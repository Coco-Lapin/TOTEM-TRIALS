package com.totemtrials.totemtrials.view;

import com.totemtrials.totemtrials.model.StatistiquesJoueur;
import com.totemtrials.totemtrials.model.StatistiquesPartie;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;

/**
 * Écran de fin de partie.
 * Layout :
 *   [ TITRE ]
 *   [ PODIUM (3 marches animées) ]
 *   [ STATS GLOBALES | TABLEAU JOUEURS ]
 *   [ BOUTONS ]
 */
public class FinPartieView {

    private final Scene  scene;
    private final Button btnRejouer;
    private final Button btnQuitter;

    // ---- Hauteurs des marches (px) ----
    private static final int H_OR     = 220;
    private static final int H_ARGENT = 170;
    private static final int H_BRONZE = 130;
    private static final int W_MARCHE = 170;

    public FinPartieView(Stage stage, StatistiquesPartie stats, Image background) {

        // ══════════════════════════════════════════
        //  FOND
        // ══════════════════════════════════════════
        ImageView bg = new ImageView(background);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        // Voile sombre pour améliorer la lisibilité
        Rectangle overlay = new Rectangle();
        overlay.setFill(Color.web("#050E02", 0.72));
        overlay.widthProperty().bind(stage.widthProperty());
        overlay.heightProperty().bind(stage.heightProperty());

        // ══════════════════════════════════════════
        //  TITRE
        // ══════════════════════════════════════════
        Label titre = new Label("TOTEM TRIALS");
        titre.getStyleClass().add("titre-fin");

        Label ssTitre = new Label("— FIN DE PARTIE —   ⏱  " + stats.getDureeFormatee());
        ssTitre.getStyleClass().add("sous-titre");

        VBox headerBox = new VBox(4, titre, ssTitre);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(30, 0, 0, 0));

        // ══════════════════════════════════════════
        //  PODIUM
        // ══════════════════════════════════════════
        StatistiquesJoueur[] classement = stats.getClassement();
        HBox podium = buildPodium(stage, classement);
        podium.setAlignment(Pos.BOTTOM_CENTER);
        podium.setPadding(new Insets(10, 0, 0, 0));

        // ══════════════════════════════════════════
        //  STATS GLOBALES + TABLEAU
        // ══════════════════════════════════════════
        HBox statsZone = buildStatsZone(stats, classement);

        // ══════════════════════════════════════════
        //  BOUTONS
        // ══════════════════════════════════════════
        btnRejouer = new Button("⟳  REJOUER");
        btnRejouer.getStyleClass().add("btn-rejouer");

        btnQuitter = new Button("✕  QUITTER");
        btnQuitter.getStyleClass().add("btn-quitter");

        HBox btnBox = new HBox(20, btnRejouer, btnQuitter);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setPadding(new Insets(16, 0, 24, 0));

        // ══════════════════════════════════════════
        //  ASSEMBLAGE
        // ══════════════════════════════════════════
        VBox content = new VBox(0, headerBox, podium, statsZone, btnBox);
        content.setAlignment(Pos.TOP_CENTER);
        content.setMaxWidth(900);
        content.setPadding(new Insets(0, 20, 0, 20));

        // Scroll implicite via un VBox scrollable si la fenêtre est petite
        ScrollableStackWrapper wrapper = new ScrollableStackWrapper(content);

        StackPane root = new StackPane(bg, overlay, wrapper.getNode());
        StackPane.setAlignment(wrapper.getNode(), Pos.TOP_CENTER);

        scene = new Scene(root, 1000, 700);

        var css = FinPartieView.class.getResource("/com/totemtrials/totemtrials/styles/finpartie.css");
        if (css != null) scene.getStylesheets().add(css.toExternalForm());

        // ══════════════════════════════════════════
        //  ANIMATIONS D'ENTRÉE
        // ══════════════════════════════════════════
        playEntranceAnimations(titre, ssTitre, podium, statsZone, btnBox);
    }

    // ─────────────────────────────────────────────
    //  CONSTRUCTION DU PODIUM
    // ─────────────────────────────────────────────
    private HBox buildPodium(Stage stage, StatistiquesJoueur[] classement) {
        // Ordre d'affichage : 2ème | 1er | 3ème
        int[] ordre = {1, 0, 2};
        String[] styles = {"marche-argent", "marche-or", "marche-bronze"};
        String[] rangStyles = {"rang-podium-argent", "rang-podium-or", "rang-podium-bronze"};
        int[] hauteurs = {H_ARGENT, H_OR, H_BRONZE};
        String[] medailles = {"🥈", "🥇", "🥉"};

        HBox row = new HBox(0);
        row.setAlignment(Pos.BOTTOM_CENTER);

        for (int i = 0; i < 3; i++) {
            int idx = ordre[i];
            if (idx >= classement.length) {
                // Colonne vide si moins de 3 joueurs
                row.getChildren().add(buildMarcheVide(hauteurs[i]));
                continue;
            }
            StatistiquesJoueur sj = classement[idx];
            row.getChildren().add(buildMarche(stage, sj, styles[i], rangStyles[i],
                                               hauteurs[i], medailles[i]));
        }
        return row;
    }

    private VBox buildMarche(Stage stage, StatistiquesJoueur sj,
                              String marcheStyle, String rangStyle,
                              int hauteur, String medaille) {

        // Token du joueur
        ImageView token = buildTokenView(stage, sj);

        // Médaille
        Label medailleLabel = new Label(medaille);
        medailleLabel.getStyleClass().add("medaille");

        // Nom
        Label nom = new Label(sj.getJoueur().getNom());
        nom.getStyleClass().add("nom-podium");

        VBox dessus = new VBox(4, token, medailleLabel, nom);
        dessus.setAlignment(Pos.BOTTOM_CENTER);
        dessus.setPadding(new Insets(0, 0, 8, 0));

        // Rang sur la marche
        Label rang = new Label(String.valueOf(sj.getPosition()));
        rang.getStyleClass().add(rangStyle);

        VBox marche = new VBox(0, rang);
        marche.setAlignment(Pos.CENTER);
        marche.setPrefWidth(W_MARCHE);
        marche.setPrefHeight(hauteur);
        marche.getStyleClass().addAll("marche", marcheStyle);

        VBox colonne = new VBox(0, dessus, marche);
        colonne.setAlignment(Pos.BOTTOM_CENTER);
        return colonne;
    }

    private Region buildMarcheVide(int hauteur) {
        Region r = new Region();
        r.setPrefWidth(W_MARCHE);
        r.setPrefHeight(hauteur);
        return r;
    }

    private ImageView buildTokenView(Stage stage, StatistiquesJoueur sj) {
        if (sj.getJoueur().getJeton() == null) {
            // Pas de jeton → placeholder
            ImageView iv = new ImageView();
            iv.setFitWidth(64);
            iv.setFitHeight(64);
            return iv;
        }
        String path = sj.getJoueur().getJeton().getImagePath();
        InputStream is = FinPartieView.class.getResourceAsStream("/" + path);
        if (is == null) {
            ImageView iv = new ImageView();
            iv.setFitWidth(64);
            return iv;
        }
        ImageView iv = new ImageView(new Image(is));
        iv.setFitWidth(72);
        iv.setPreserveRatio(true);
        // Glow animé pour le 1er
        if (sj.getPosition() == 1) {
            DropShadow glow = new javafx.scene.effect.DropShadow(20, Color.GOLD);
            glow.setSpread(0.4);
            iv.setEffect(glow);
            ScaleTransition pulse = new ScaleTransition(Duration.millis(900), iv);
            pulse.setFromX(1.0); pulse.setToX(1.12);
            pulse.setFromY(1.0); pulse.setToY(1.12);
            pulse.setCycleCount(Timeline.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.play();
        }
        return iv;
    }

    // ─────────────────────────────────────────────
    //  STATS ZONE
    // ─────────────────────────────────────────────
    private HBox buildStatsZone(StatistiquesPartie stats, StatistiquesJoueur[] classement) {
        VBox globales = buildStatsGlobales(stats);
        VBox tableau  = buildTableauJoueurs(classement);

        Separator sep = new Separator(javafx.geometry.Orientation.VERTICAL);
        sep.getStyleClass().add("separateur-stats");

        HBox zone = new HBox(30, globales, sep, tableau);
        zone.setAlignment(Pos.TOP_CENTER);
        zone.getStyleClass().add("stats-panel");
        zone.setMaxWidth(860);
        HBox.setHgrow(tableau, Priority.ALWAYS);
        VBox.setMargin(zone, new Insets(12, 0, 8, 0));
        return zone;
    }

    private VBox buildStatsGlobales(StatistiquesPartie stats) {
        Label titre = new Label("PARTIE");
        titre.getStyleClass().add("stats-section-title");

        VBox col = new VBox(12, titre);
        col.setAlignment(Pos.TOP_CENTER);
        col.setPrefWidth(160);

        col.getChildren().addAll(
            statGlobale(stats.getDureeFormatee(),     "DURÉE"),
            statGlobale(String.valueOf(stats.getTotalTours()),     "TOURS"),
            statGlobale(String.valueOf(stats.getTotalQuestions()), "QUESTIONS")
        );
        return col;
    }

    private VBox statGlobale(String valeur, String libelle) {
        Label v = new Label(valeur);
        v.getStyleClass().add("stat-globale-valeur");
        Label l = new Label(libelle);
        l.getStyleClass().add("stat-globale-label");
        VBox box = new VBox(0, v, l);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox buildTableauJoueurs(StatistiquesJoueur[] classement) {
        Label titre = new Label("JOUEURS");
        titre.getStyleClass().add("stats-section-title");

        // En-tête
        HBox header = new HBox(
            colHeader("#",    35),
            colHeader("NOM",       160),
            colHeader("TOURS",      65),
            colHeader("✔ BON",      70),
            colHeader("✘ RATÉ",     70),
            colHeader("RÉUSSITE",   80)
        );
        header.setAlignment(Pos.CENTER_LEFT);

        VBox tableau = new VBox(6, titre, header);
        tableau.setAlignment(Pos.TOP_LEFT);

        for (StatistiquesJoueur sj : classement) {
            boolean winner = sj.getPosition() == 1;

            Label rang   = styledCell(sj.getPosition() == 1 ? "🏆" :
                                      sj.getPosition() == 2 ? "🥈" : "🥉", "cell-valeur", 35);
            Label nom    = styledCell(sj.getJoueur().getNom(), "cell-nom", 160);
            Label tours  = styledCell(String.valueOf(sj.getNombreTours()),         "cell-valeur", 65);
            Label bon    = styledCell(String.valueOf(sj.getBonnesReponses()),       "cell-bon",    70);
            Label mauvais= styledCell(String.valueOf(sj.getMauvaisesReponses()),    "cell-mauvais",70);
            Label pct    = styledCell(sj.getPourcentageReussite() + "%",            "cell-pct",    80);

            HBox row = new HBox(rang, nom, tours, bon, mauvais, pct);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add(winner ? "row-joueur-winner" : "row-joueur");
            VBox.setMargin(row, new Insets(2, 0, 2, 0));

            tableau.getChildren().add(row);
        }

        return tableau;
    }

    private Label colHeader(String text, double width) {
        Label l = new Label(text);
        l.getStyleClass().add("table-header");
        l.setPrefWidth(width);
        l.setMinWidth(width);
        return l;
    }

    private Label styledCell(String text, String style, double width) {
        Label l = new Label(text);
        l.getStyleClass().add(style);
        l.setPrefWidth(width);
        l.setMinWidth(width);
        l.setPadding(new Insets(0, 8, 0, 8));
        return l;
    }

    // ─────────────────────────────────────────────
    //  ANIMATIONS D'ENTRÉE
    // ─────────────────────────────────────────────
    private void playEntranceAnimations(javafx.scene.Node... nodes) {
        for (int i = 0; i < nodes.length; i++) {
            javafx.scene.Node node = nodes[i];
            node.setOpacity(0);
            node.setTranslateY(30);

            FadeTransition ft = new FadeTransition(Duration.millis(600), node);
            ft.setToValue(1);

            TranslateTransition tt = new TranslateTransition(Duration.millis(600), node);
            tt.setToY(0);

            ParallelTransition pt = new ParallelTransition(ft, tt);
            pt.setDelay(Duration.millis(150 + i * 200L));
            pt.play();
        }
    }

    // ─────────────────────────────────────────────
    //  ACCESSEURS
    // ─────────────────────────────────────────────
    public Scene  getScene()      { return scene; }
    public Button getBtnRejouer() { return btnRejouer; }
    public Button getBtnQuitter() { return btnQuitter; }

    // ─────────────────────────────────────────────
    //  Helper interne : wrap scrollable si besoin
    // ─────────────────────────────────────────────
    private static class ScrollableStackWrapper {
        private final javafx.scene.control.ScrollPane sp;

        ScrollableStackWrapper(VBox content) {
            sp = new javafx.scene.control.ScrollPane(content);
            sp.setFitToWidth(true);
            sp.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
            sp.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
            sp.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            sp.setMaxWidth(Double.MAX_VALUE);
        }

        javafx.scene.Node getNode() { return sp; }
    }
}
