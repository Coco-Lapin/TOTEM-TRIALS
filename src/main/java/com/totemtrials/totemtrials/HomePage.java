package com.totemtrials.totemtrials;

import java.io.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static javafx.application.Platform.exit;

public class HomePage extends Application {

    private Stage stage;
    private Scene previousScene;
    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        // ------------------- IMAGE -------------------
        // Utilise getResourceAsStream pour charger l'image depuis le classpath
        String path = "backgroundMenu.png";
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

        try {
            String musicFile = getClass().getResource("sounds/Agrual.mp3").toURI().toString();
            Media sound = new Media(musicFile);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception ex) {
            System.err.println("Erreur chargement audio : " + ex.getMessage());
        }

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
        Button backbutton = new Button("BACK");
        Button quitbutton = new Button("QUIT");

        // ------------------- SCENE -------------------
        //creation de l'endroit par
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 600, 500);
        this.previousScene = scene;

        //creation d'une vbox pour les boutons - argument : espacement entre le boutons
        VBox fenetre = new VBox(10);
        //ajout des boutons dans la vbox
        fenetre.getChildren().addAll(playbutton, optionbutton, quitbutton);
        fenetre.setAlignment(Pos.CENTER); // Centre les enfants dans le VBox
        fenetre.setFillWidth(false);      // Empêche les boutons de s'étirer sur toute la largeur

        //ajout de la vbox dans stackpane
        root.getChildren().add(fenetre);
        //creation de la scene
        stage.setScene(scene);

        // ------------------- ACTIONS -------------------
        //place l'action quitter sur le quitbutton
        quitbutton.setOnAction(this::quit);

        //bouton retour a placer dans les autres fenetres (options + jeu)
        //le backbutton permet de revenir a la fenetre precedente donné en arguement (menuScene)
        //setOnAction - permet de faire l'action quand le bouton est cliqué
        //e est l'event
        //stage.setScene(scene) - remplace la scene actuelle par scene
        backbutton.setOnAction(e -> stage.setScene(scene));

        // ------------------- STAGE -------------------
        stage.setTitle("Menu principal - demarrage du jeu");
        //permet de mettre la box en full screen
        stage.setFullScreen(true);
        stage.show();
    }

    public void quit(ActionEvent e) {
        Platform.exit();
    }

    public void back(ActionEvent e) {
        stage.setScene(previousScene);
    }
}