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

import java.util.ArrayList;
import java.util.List;


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


        // Ma liste de cases "logiques"
        private List<Case> listeCases = new ArrayList<>();

        // Dimensions fixes et réelles de ton terrain de jeu
        private final double LARGEUR_REELLE = 6144.0;
        private final double HAUTEUR_REELLE = 3584.0;
        public String determinerType(Rectangle r) {
            String id = r.getId(); // Récupère par exemple "RDiv1" ou "RTour3"

            if (id.startsWith("RDiv"))   return "Divertissement";
            if (id.startsWith("RTour"))  return "Tourism";
            if (id.startsWith("RInfo"))  return "INFO";
            if (id.startsWith("RMyst"))  return "MYSTERY";
            if (id.startsWith("RVersus")) return "VERSUS";
            if (id.startsWith("RHop"))    return "HOP";
            if (id.equals("RStart"))     return "DEPART";
            if (id.equals("RFinish"))    return "FINISH";

            return "INCONNU"; // Sécurité
        }


        @FXML
        public void initialize() {
            Platform.runLater(() -> {


                Rectangle [] cheminDuJeu={RStart , RDiv1, RInfo1, RMyst1,RTour1,RVersus1,RHop1,
                        RDiv2,RInfo2,RMyst2,RTour2,RHop2,RVersus2,RDiv3,RInfo3,RMyst3,RTour3,
                        RDiv4,RInfo4,RMyst4,RTour4,RDiv5,RInfo5,RHop3,RMyst5,RTour5,
                        RDiv6,RInfo6,RVersus3,RHop4,RMyst6,RTour6,RDiv7,RInfo7,RMyst7,RTour7,
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
                Rectangle[] Hop ={RHop1, RHop2,RHop3,RHop4};
                for(Rectangle r : Hop){
                    r.setFill(patternHop);
                }
                /*
                // On crée les objets Case en reliant le visuel au logique
                // Ici on passe l'ID, le Type et la référence au Rectangle FXML
                // case START ND FINISH
                listeCases.add(new Case("START", "DEPART", RStart));
                listeCases.add(new Case("FINISH", "FINISH", RFinish));
                // cases Divertissement
                listeCases.add(new Case("DIV1", "Divertissement", RDiv1));
                listeCases.add(new Case("DIV2", "Divertissement", RDiv2));
                listeCases.add(new Case("DIV3", "Divertissement", RDiv3));
                listeCases.add(new Case("DIV4", "Divertissement", RDiv4));
                listeCases.add(new Case("DIV5", "Divertissement", RDiv5));
                listeCases.add(new Case("DIV6", "Divertissement", RDiv6));
                listeCases.add(new Case("DIV7", "Divertissement", RDiv7));
                listeCases.add(new Case("DIV8", "Divertissement", RDiv8));
                // cases Informatic
                listeCases.add(new Case("INFO1", "INFO", RInfo1));
                listeCases.add(new Case("INFO2", "INFO", RInfo2));
                listeCases.add(new Case("INFO3", "INFO", RInfo3));
                listeCases.add(new Case("INFO4", "INFO", RInfo4));
                listeCases.add(new Case("INFO5", "INFO", RInfo5));
                listeCases.add(new Case("INFO6", "INFO", RInfo6));
                listeCases.add(new Case("INFO7", "INFO", RInfo7));
                listeCases.add(new Case("INFO8", "INFO", RInfo8));
                // cases Tourism
                listeCases.add(new Case("TOUR1","TOURISM", RTour1));
                listeCases.add(new Case("TOUR2","TOURISM", RTour2));
                listeCases.add(new Case("TOUR3","TOURISM", RTour3));
                listeCases.add(new Case("TOUR4","TOURISM", RTour4));
                listeCases.add(new Case("TOUR5","TOURISM", RTour5));
                listeCases.add(new Case("TOUR6","TOURISM", RTour6));
                listeCases.add(new Case("TOUR7","TOURISM", RTour7));
                listeCases.add(new Case("TOUR8","TOURISM", RTour8));
                //case Mystery
                listeCases.add(new Case("Myst1","MYSTERY", RMyst1));
                listeCases.add(new Case("Myst2", "MYSTERY", RMyst2));
                listeCases.add(new Case("Myst3", "MYSTERY", RMyst3));
                listeCases.add(new Case("Myst4", "MYSTERY", RMyst4));
                listeCases.add(new Case("Myst5", "MYSTERY", RMyst5));
                listeCases.add(new Case("Myst6", "MYSTERY", RMyst6));
                listeCases.add(new Case("Myst7", "MYSTERY", RMyst7));
                listeCases.add(new Case("Myst8", "MYSTERY", RMyst8));
                // cases HOP et VERSUS
                listeCases.add(new Case("HOP1", "HOP", RHop1));
                listeCases.add(new Case("HOP2", "HOP", RHop2));
                listeCases.add(new Case("HOP3", "HOP", RHop3));
                listeCases.add(new Case("HOP4", "HOP", RHop4));
                listeCases.add(new Case("VERS1", "VERSUS", RVersus1));
                listeCases.add(new Case("VERS2", "VERSUS", RVersus2));
                listeCases.add(new Case("VERS3", "VERSUS", RVersus3));
                listeCases.add(new Case("VERS4", "VERSUS", RVersus4));

            */
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
        }

    public void infoItem(ActionEvent actionEvent) {
    }
}

