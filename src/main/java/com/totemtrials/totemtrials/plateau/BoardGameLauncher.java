package com.totemtrials.totemtrials.plateau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



import static javafx.application.Application.launch;

public class BoardGameLauncher extends Application {




 //lien à MODIF

        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Plateau.fxml"));
            AnchorPane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            // 1. AFFICHAGE MAXIMISÉ (Prend tout l'écran avec la barre de tâches)
            primaryStage.setMaximized(true);

            // 2. SÉCURITÉ : On définit une taille minimale pour éviter que tout disparaisse
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            primaryStage.setTitle("Mon Plateau de Jeu");
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

