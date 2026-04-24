package com.totemtrials.totemtrials.plateau;

import com.totemtrials.totemtrials.controller.movementController;
import com.totemtrials.totemtrials.models.GameManager;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.List;
// import du quiz
import com.totemtrials.totemtrials.questions.GestionQuiz;
import com.totemtrials.totemtrials.HelloApplication;

public class BoardGameController {
    public VBox PlateauInfoLeft;
    public Button btnSettings;
    public Label ItemDescription;
    public Button btnDetailsItem;
    public Button btnStopGame;
    private movementController MC;
    public Label labelRound;
    public ImageView imageFond;
    private BoardGameLauncher BoardGameLauncher;


    @FXML
        private StackPane zoneCentrale;
        @FXML private Pane plateauJeu;

        // Injection de tous tes rectangles depuis le FXML
        @FXML private Rectangle RStart, RFinish;
        @FXML private Rectangle RDiv1, RDiv2, RDiv3, RDiv4, RDiv5, RDiv6, RDiv7, RDiv8;
        @FXML private Rectangle RInfo1, RInfo2, RInfo3, RInfo4, RInfo5, RInfo6, RInfo7, RInfo8;
        @FXML private Rectangle RTour1, RTour2, RTour3, RTour4, RTour5, RTour6, RTour7, RTour8;
        @FXML private Rectangle RMyst1, RMyst2, RMyst3, RMyst4, RMyst5, RMyst6, RMyst7, RMyst8;
        @FXML private Rectangle RVersus1, RVersus2,RVersus3,RVersus4, RHop1 , RHop2 , RBonus1, RBonus2 ;

        // Ma liste de cases "logiques"
        private final List<Case> listeCases = new ArrayList<>();

        // Dimensions fixes et réelles de ton terrain de jeu
        private final double LARGEUR_REELLE = 6144.0;
        private final double HAUTEUR_REELLE = 3584.0;
        public String determinerType(Rectangle r) {
            String id = r.getId(); // Récupère par exemple "RDiv1" ou "RTour3"

            if (id.startsWith("RDiv"))   return "entertainment";
            if (id.startsWith("RTour"))  return "Tourism";
            if (id.startsWith("RInfo"))  return "Informatics";
            if (id.startsWith("RMyst"))  return "Mystery (Jumanji)";
            if (id.startsWith("RVersus")) return "VERSUS";
            if (id.startsWith("RHop"))    return "HOP";
            if (id.equals("RStart"))     return "DEPART";
            if (id.equals("RFinish"))    return "FINISH";

            return "INCONNU"; // Sécurité
        }
        List<Rectangle> cheminDuJeu;

        @FXML
        public void initialize() {
            // CRUCIAL : On crée l'objet ici
            this.MC = new movementController();

            // 3. Maintenant on peut appeler les méthodes dessus sans erreur
            this.MC.setBoardGame(this);
            this.MC.setPlateauJeu(this.plateauJeu);
            Platform.runLater(() -> {

                Rectangle [] cheminDuJeu={RStart , RDiv1, RInfo1, RMyst1,RTour1,RVersus1,RHop1,
                        RDiv2,RInfo2,RMyst2,RTour2,RBonus1,RVersus2,RDiv3,RInfo3,RMyst3,RTour3,
                        RDiv4,RInfo4,RMyst4,RTour4,RDiv5,RInfo5,RHop2,RMyst5,RTour5,
                        RDiv6,RInfo6,RVersus3,RBonus2,RMyst6,RTour6,RDiv7,RInfo7,RMyst7,RTour7,
                        RDiv8,RVersus4,RInfo8,RMyst8,RTour8,RFinish
                };
                // 1. On calcule le ratio parfait pour que tout rentre dans l'écran
                DoubleBinding ratioZoom = Bindings.createDoubleBinding(() -> {
                    // Combien de fois l'écran est plus petit que l'image ?
                    double ratioX = zoneCentrale.getWidth() / LARGEUR_REELLE;
                    double ratioY = zoneCentrale.getHeight() / HAUTEUR_REELLE;
                    // On prend le plus petit des deux ratios pour ne pas déborder (équivalent du PreserveRatio)
                    return Math.min(ratioX, ratioY);
                }, zoneCentrale.widthProperty(), zoneCentrale.heightProperty());

                // 2. On applique ce zoom au plateau entier (Image + Cases en même temps !)
                plateauJeu.scaleXProperty().bind(ratioZoom);
                plateauJeu.scaleYProperty().bind(ratioZoom);

                Image imgTour = new Image(getClass().getResource("/images/Tourism.png").toExternalForm());
                ImagePattern patternTour = new ImagePattern(imgTour);

                // 2. Créer un tableau contenant tes variables @FXML
                Rectangle[] Tourism = {RTour1, RTour2, RTour3, RTour4, RTour5, RTour6, RTour7, RTour8};

                // 3. Boucler sur le tableau en toute sécurité
                for (Rectangle r : Tourism) {
                    if (r != null) {
                        r.setFill(patternTour);
                    }
                }
                Image imgDiv = new Image(getClass().getResource("/images/Divert.png").toExternalForm());
                ImagePattern patternDiv = new ImagePattern(imgDiv);
                Rectangle[] Divert ={RDiv1, RDiv2, RDiv3, RDiv4, RDiv5, RDiv6, RDiv7, RDiv8};
                for (Rectangle r : Divert) {
                    if (r != null) {
                        r.setFill(patternDiv);
                    }
                }
                Image imgMyst = new Image(getClass().getResource("/images/Mistery.png").toExternalForm());
                ImagePattern patternMyst = new ImagePattern(imgMyst);
                Rectangle[] Myst ={RMyst1, RMyst2, RMyst3, RMyst4, RMyst5, RMyst6, RMyst7, RMyst8};
                for (Rectangle r : Myst) {
                    if (r != null) {
                        r.setFill(patternMyst);
                    }
                }
                Image imgInfo = new Image(getClass().getResource("/images/Informatic.png").toExternalForm());
                ImagePattern patternInfo = new ImagePattern(imgInfo);
                Rectangle[] Info ={RInfo1, RInfo2, RInfo3, RInfo4, RInfo5, RInfo6, RInfo7, RInfo8};
                for (Rectangle r : Info) {
                    if (r != null) {
                        r.setFill(patternInfo);
                    }
                }
                Image imgVersus = new Image(getClass().getResource("/images/Versus.png").toExternalForm());
                ImagePattern patterVersus = new ImagePattern(imgVersus);
                Rectangle[] Versus ={RVersus1, RVersus2,RVersus3,RVersus4};
                for(Rectangle r : Versus){
                    r.setFill(patterVersus);
                }
                Image imgHop = new Image(getClass().getResource("/images/HOP.png").toExternalForm());
                ImagePattern patternHop = new ImagePattern(imgHop);
                Rectangle[] Hop ={RHop1, RHop2,RBonus1,RBonus2};
                for(Rectangle r : Hop){
                    r.setFill(patternHop);
                }

                Image imgStart = new Image(getClass().getResource("/images/start.png").toExternalForm());
                RStart.setFill(new ImagePattern(imgStart));

                Image imgEnd = new Image(getClass().getResource("/images/end.png").toExternalForm());
                RFinish.setFill(new ImagePattern(imgEnd));

                // Dans ton initialize, tu boucles sur CE tableau
                for (int i = 0; i < cheminDuJeu.length; i++) {
                    Rectangle r = cheminDuJeu[i];
                    // Ici, tu peux déterminer le type selon le nom du rectangle ou sa couleur
                    String type = determinerType(r);
                    listeCases.add(new Case("Case" + i, type, r));
                }
                int k = 1;
                // Exemple : Vérifier que le calcul du centre fonctionne
                for (Case c : listeCases) {
                    System.out.println("Case n°: "+ k + c.getId() + " centrée en : " + c.getCenterX() + ", " + c.getCenterY());
                    k++;
                }
            });
            GameManager gm = new GameManager(this, this.MC, this.listeCases);
            gm.demarrerPartie();
        }
    public void afficherPopUpQuiz(StackPane quizVue) {
        quizVue.setMaxSize(400, 500);
        StackPane.setAlignment(quizVue, Pos.CENTER);
        zoneCentrale.getChildren().add(quizVue);
    }

    public void fermerPopUpQuiz(StackPane window) {
        zoneCentrale.getChildren().remove(window);
    }
    public List<Case> getListeCases() {
        return listeCases;
    }

    public String getTileTheme(int nb){
        return listeCases.get(nb).getType();
    }
    public void infoItem(ActionEvent actionEvent) {
    }

    public void StopGame(ActionEvent actionEvent) {
        // le node permet de dire au getSource que c'est un objet JAVAFX
        // et permet d'accèder au méthodes spécifiques ( getScene())
        // getWindow() récupère l'objet Window qui contient tout
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void OpenSettings(ActionEvent actionEvent) {
    }

    // Expose zoneCentrale pour le binding adaptatif dans GestionQuiz
    public StackPane getZoneCentrale() {
        return zoneCentrale;
    }
}

