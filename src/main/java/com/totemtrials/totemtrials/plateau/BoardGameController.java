package com.totemtrials.totemtrials.plateau;

import com.totemtrials.totemtrials.controller.OptionsController;
import com.totemtrials.totemtrials.controller.SceneManager;
import com.totemtrials.totemtrials.controller.movementController;
import com.totemtrials.totemtrials.models.GameConfig;
import com.totemtrials.totemtrials.models.GameManager;
import com.totemtrials.totemtrials.view.OptionsView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import com.totemtrials.totemtrials.questions.GestionQuiz;

public class BoardGameController {

    public VBox PlateauInfoLeft;
    public Button btnSettings;
    public Label ItemDescription;
    public Button btnDetailsItem;
    public Button btnStopGame;
    public Label labelRound;
    public ImageView imageFond;

    @FXML private StackPane zoneCentrale;
    @FXML private Pane      plateauJeu;

    @FXML private Rectangle RStart, RFinish;
    @FXML private Rectangle RDiv1,  RDiv2,  RDiv3,  RDiv4,  RDiv5,  RDiv6,  RDiv7,  RDiv8;
    @FXML private Rectangle RInfo1, RInfo2, RInfo3, RInfo4, RInfo5, RInfo6, RInfo7, RInfo8;
    @FXML private Rectangle RTour1, RTour2, RTour3, RTour4, RTour5, RTour6, RTour7, RTour8;
    @FXML private Rectangle RMyst1, RMyst2, RMyst3, RMyst4, RMyst5, RMyst6, RMyst7, RMyst8;
    @FXML private Rectangle RVersus1, RVersus2, RVersus3, RVersus4;
    @FXML private Rectangle RHop1, RHop2, RBonus1, RBonus2;

    private final List<Case> listeCases = new ArrayList<>();
    private movementController MC;

    private static final double LARGEUR_REELLE = 6144.0;
    private static final double HAUTEUR_REELLE = 3584.0;

    private static final String IMG_TILES = "/images/gameSquare/";
    private static final String IMG_BOARD = "/images/gameSquare/";

    public String determinerType(Rectangle r) {
        String id = r.getId();
        if (id.startsWith("RDiv"))    return "entertainment";
        if (id.startsWith("RTour"))   return "Tourism";
        if (id.startsWith("RInfo"))   return "Computing";
        if (id.startsWith("RMyst"))   return "Mystery (Jumanji)";
        if (id.startsWith("RVersus")) return "VERSUS";
        if (id.startsWith("RHop"))    return "HOP";
        if (id.equals("RStart"))      return "DEPART";
        if (id.equals("RFinish"))     return "FINISH";
        if (id.startsWith("RBonus"))  return "BONUS";
        return "INCONNU";
    }

    @FXML
    public void initialize() {
        this.MC = new movementController();
        this.MC.setBoardGame(this);
        this.MC.setPlateauJeu(this.plateauJeu);
        this.MC.setupPlayers(GameConfig.getInstance().getNbJoueurs());

        Platform.runLater(() -> {
            Rectangle[] cheminDuJeu = {
                    RStart,
                    RDiv1, RInfo1, RMyst1, RTour1, RVersus1, RHop1,
                    RDiv2, RInfo2, RMyst2, RTour2, RBonus1, RVersus2,
                    RDiv3, RInfo3, RMyst3, RTour3,
                    RDiv4, RInfo4, RMyst4, RTour4,
                    RDiv5, RInfo5, RHop2, RMyst5, RTour5,
                    RDiv6, RInfo6, RVersus3, RBonus2, RMyst6, RTour6,
                    RDiv7, RInfo7, RMyst7, RTour7,
                    RDiv8, RVersus4, RInfo8, RMyst8, RTour8,
                    RFinish
            };

            // ── Zoom adaptatif ──────────────────────────────────────────
            DoubleBinding ratioZoom = Bindings.createDoubleBinding(() -> {
                double ratioX = zoneCentrale.getWidth()  / LARGEUR_REELLE;
                double ratioY = zoneCentrale.getHeight() / HAUTEUR_REELLE;
                return Math.min(ratioX, ratioY);
            }, zoneCentrale.widthProperty(), zoneCentrale.heightProperty());

            plateauJeu.scaleXProperty().bind(ratioZoom);
            plateauJeu.scaleYProperty().bind(ratioZoom);

            // ── Fond de remplissage responsive (remplace les bandes bleues) ──
            Image imgBg = load("/images/plateau-jeu-javaFX.png");
            if (imgBg != null) {
                ImageView bgFill = new ImageView(imgBg);
                bgFill.fitWidthProperty().bind(zoneCentrale.widthProperty());
                bgFill.fitHeightProperty().bind(zoneCentrale.heightProperty());
                bgFill.setPreserveRatio(false);
                bgFill.setOpacity(0.55); // Assombri pour ne pas concurrencer le plateau réel
                zoneCentrale.getChildren().add(0, bgFill); // index 0 = derrière tout
            }

            // ── Textures des cases ──────────────────────────────────────
            applyPattern(new Rectangle[]{RTour1,RTour2,RTour3,RTour4,RTour5,RTour6,RTour7,RTour8},
                    IMG_TILES + "Tourism.png");
            applyPattern(new Rectangle[]{RDiv1,RDiv2,RDiv3,RDiv4,RDiv5,RDiv6,RDiv7,RDiv8},
                    IMG_TILES + "Divert.png");
            applyPattern(new Rectangle[]{RMyst1,RMyst2,RMyst3,RMyst4,RMyst5,RMyst6,RMyst7,RMyst8},
                    IMG_TILES + "Mistery.png");
            applyPattern(new Rectangle[]{RInfo1,RInfo2,RInfo3,RInfo4,RInfo5,RInfo6,RInfo7,RInfo8},
                    IMG_TILES + "computing.png");   // FIX: Informatic.png → computing.png
            applyPattern(new Rectangle[]{RVersus1,RVersus2,RVersus3,RVersus4},
                    IMG_TILES + "Versus.png");
            applyPattern(new Rectangle[]{RHop1,RHop2,RBonus1,RBonus2},
                    IMG_TILES + "HOP.png");

            RStart.setFill(new ImagePattern(load(IMG_BOARD + "Start.png")));
            RFinish.setFill(new ImagePattern(load(IMG_BOARD + "Finish.png")));

            // ── Construction de la liste logique ────────────────────────
            for (int i = 0; i < cheminDuJeu.length; i++) {
                Rectangle r = cheminDuJeu[i];
                listeCases.add(new Case("Case" + i, determinerType(r), r));
            }

            int k = 1;
            for (Case c : listeCases) {
                System.out.println("Case n°" + k++ + " " + c.getId()
                        + " centre: " + c.getCenterX() + ", " + c.getCenterY());
            }
        });

        GameManager gm = new GameManager(this, this.MC, this.listeCases);
        String css = getClass().getResource("/styleSheet/homepage.css").toExternalForm();
        plateauJeu.getScene(); // scene peut être null ici — utilise Platform.runLater déjà en place
        gm.demarrerPartie();
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    /** Charge une image — log + retourne null si introuvable (pas de NPE cascade). */
    private Image load(String path) {
        var url = getClass().getResource(path);
        if (url == null) {
            System.err.println("[BoardGameController] Image introuvable : " + path);
            return null;
        }
        return new Image(url.toExternalForm());
    }

    private void applyPattern(Rectangle[] targets, String path) {
        Image img = load(path);
        if (img == null) return; // échec silencieux mais loggé, n'interrompt pas la suite
        ImagePattern pattern = new ImagePattern(img);
        for (Rectangle r : targets) {
            if (r != null) r.setFill(pattern);
        }
    }

    // ── Quiz popup ──────────────────────────────────────────────────────────

    public void afficherPopUpQuiz(StackPane quizVue) {
        quizVue.setMaxSize(400, 500);
        StackPane.setAlignment(quizVue, Pos.CENTER);
        zoneCentrale.getChildren().add(quizVue);
    }

    public void fermerPopUpQuiz(StackPane window) {
        zoneCentrale.getChildren().remove(window);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public void infoItem(ActionEvent actionEvent) { }

    public void StopGame(ActionEvent actionEvent) {
        // Retour au menu principal au lieu de fermer la fenêtre
        SceneManager.show(SceneManager.getHomeScene(), "Menu principal");
    }

    public void OpenSettings(ActionEvent actionEvent) {
        OptionsView optView = new OptionsView(SceneManager.getStage(), imageFond.getImage());
        Scene scenePlateau = ((Node) actionEvent.getSource()).getScene();
        new OptionsController(optView, scenePlateau, "Totem Trials", SceneManager.getPlayer());
        SceneManager.show(optView.getScene(), "Options");
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public List<Case> getListeCases() { return listeCases; }
    public String getTileTheme(int nb) { return listeCases.get(nb).getType(); }
    public StackPane getZoneCentrale() { return zoneCentrale; }
}
// ── PATCH StopGame — remplacer la méthode existante dans BoardGameController_v2.java ──
// public void StopGame(ActionEvent actionEvent) {
//     SceneManager.show(SceneManager.getHomeScene(), "Menu principal");
// }