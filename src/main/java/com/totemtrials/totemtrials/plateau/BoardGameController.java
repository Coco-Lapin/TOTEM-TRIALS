package com.totemtrials.totemtrials.plateau;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;




public class BoardGameController {

    public Label labelRound;
    public ImageView imageFond;
    @FXML
        private StackPane zoneCentrale;
        @FXML private Pane plateauJeu;

        // Injection de tous tes rectangles depuis le FXML
        @FXML private Rectangle RStart, RFinish;
        @FXML private Rectangle RDiv1, RDiv2, RDiv3, RDiv4, RDiv5, RDiv6, RDiv7, RDiv8;
        @FXML private Rectangle RInfo1, RInfo2, RInfo3, RInfo4, RInfo5, RInfo6, RInfo7, RInfo8;
        @FXML private Rectangle RTour1, RTour2, RTour3, RTour4, RTour5, RTour6, RTour7, RTour8;
        @FXML private Rectangle RMyst1, RMyst2, RMyst3, RMyst4, RMyst5, RMyst6, RMyst7, RMyst8;
        @FXML private Rectangle RVersus1, RVersus2,RVersus3,RVersus4, RHop1 , RHop2 , RHop3 , RHop4 ;



        // Dimensions fixes et réelles de ton terrain de jeu
        private final double LARGEUR_REELLE = 6144.0;
        private final double HAUTEUR_REELLE = 3584.0;

        @FXML
        public void initialize() {
            Platform.runLater(() -> {

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
                Rectangle[] Hop ={RHop1, RHop2,RHop3,RHop4};
                for(Rectangle r : Hop){
                    r.setFill(patternHop);
                }

            });
        }

    public void infoItem(ActionEvent actionEvent) {
    }
}

