package com.totemtrials.totemtrials;

import java.io.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HomePage extends Application {
    @Override
    public void start(Stage stage) {

        // Utilise getResourceAsStream pour charger l'image depuis le classpath
        String path = "imageFondTest.png";
        InputStream is = getClass().getResourceAsStream(path);

        if (is == null) {
            System.err.println("Erreur : L'image n'a pas été trouvée à " + path);
            // Affiche la liste des ressources disponibles pour déboguer
            try (InputStream in = getClass().getResourceAsStream("/");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String resource;
                while ((resource = reader.readLine()) != null) {
                    System.err.println(resource);
                }
            } catch (Exception e) {
                System.err.println("Une erreur est survenue au niveau du chargement du fichier");
            }
            return;
        }

        // ------------------- IMAGE -------------------
        //creation de l'object image
        Image image = new Image(is);
        ImageView imageView = new ImageView(image);

        //definition de la position + taille du l'image
        //imageView.setX(50);
        //imageView.setY(25);
        //imageView.setFitHeight(455);
        //imageView.setFitWidth(500);

        //permet de mettre l'image a la taille de la box
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());

        //permet de garder le bon ratio de l'image
        imageView.setPreserveRatio(false);

        // ------------------- BOUTON -------------------
        Button playbutton = new Button("NEW GAME");
        Button optionbutton = new Button("OPTIONS");
        Button quitbutton = new Button("QUIT");

        //creation de l'endroit par
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 600, 500);
        //creation d'une vbox pour les boutons - argument : espacement entre le boutons
        VBox fenetre = new VBox(10);
        //ajout des boutons dans la vbox
        fenetre.getChildren().addAll(playbutton, optionbutton, quitbutton);
        //ajout de la vbox dans stackpane
        root.getChildren().add(fenetre);
        //creation de la scene
        stage.setScene(scene);


        fenetre.setAlignment(Pos.CENTER); // Centre les enfants dans le VBox
        fenetre.setFillWidth(false);      // Empêche les boutons de s'étirer sur toute la largeur

        stage.setTitle("Menu principal - demarrage du jeu");
        //permet de mettre la box en full screen
        stage.setFullScreen(true);
        stage.show();
    }
}