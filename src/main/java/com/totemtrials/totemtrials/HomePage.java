package com.totemtrials.totemtrials;

import java.io.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HomePage extends Application {

    private Stage stage;
    private MediaPlayer mediaPlayer;
    private Image image;

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

        // ------------------- SOUND -------------------
        try {
            String musicFile = getClass().getResource("sounds/Agrual.mp3").toURI().toString();
            Media sound = new Media(musicFile);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception ex) {
            System.err.println("Erreur chargement audio : " + ex.getMessage());
        }

        //creation de l'object image
        image = new Image(is);
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

        //creation d'une vbox pour les boutons - argument : espacement entre le boutons
        VBox fenetreBouton = new VBox(10);
        //ajout des boutons dans la vbox
        fenetreBouton.getChildren().addAll(playbutton, optionbutton, quitbutton);
        fenetreBouton.setAlignment(Pos.CENTER); // Centre les enfants dans le VBox
        fenetreBouton.setFillWidth(false);      // Empêche les boutons de s'étirer sur toute la largeur

        //ajout de la vbox dans stackpane
        root.getChildren().add(fenetreBouton);
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
        backbutton.setOnAction(e -> {
            stage.setScene(scene);
            stage.setFullScreen(true); //ne fonctionne pas le full screen en retour
        });

        Scene optionsScene = option(scene);
        optionbutton.setOnAction(e -> {
            stage.setScene(optionsScene);
            stage.setTitle("OPTIONS");
            stage.setFullScreen(true);
        });

        // ------------------- STAGE -------------------
        stage.setTitle("Menu principal - demarrage du jeu");
        //permet de mettre la box en full screen
        stage.setFullScreen(true);
        stage.show();
    }

    public Scene option(Scene menuScene) {

        // nouvelle imageview avec la meme image
        ImageView imageViewOption = new ImageView(image);
        imageViewOption.fitWidthProperty().bind(stage.widthProperty());
        imageViewOption.fitHeightProperty().bind(stage.heightProperty());
        imageViewOption.setPreserveRatio(false);

        //creation du bouton back + future slider
        Button backButton = new Button("BACK");

        // Create a slider with min=0, max=100, and value=25
        Slider volumeSlider = new Slider();
        //donne les différentes valeur au slider que l'on peut passer en argument (min,max,default value)
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(25);
        volumeSlider.prefWidthProperty().bind(stage.widthProperty().multiply(0.2));
        volumeSlider.setShowTickLabels(true); //affiche les valeurs sur le slider
        //slider.setShowTickMarks(true);  //affiche les traits pour ses valeurs
        //slider.setMajorTickUnit(25); //affiche l'espacement visuel entre chaque trait
        volumeSlider.setBlockIncrement(1); //change la valeur d'increment du slider

        //creation du vbox
        VBox fenetreOption = new VBox(10);
        fenetreOption.getChildren().addAll(volumeSlider, backButton);
        fenetreOption.setAlignment(Pos.CENTER);
        fenetreOption.setFillWidth(false);

        //ajout de l'image et de la vbox dans le stackpane
        StackPane rootOption = new StackPane(imageViewOption);
        rootOption.getChildren().add(fenetreOption);

        //creation de la scene options
        Scene optionsScene = new Scene(rootOption, 600, 500);

        // Lier le slider au volume du MediaPlayer
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty().divide(100));

        //donne l'action au bouton back
        backButton.setOnAction(e -> {
            stage.setScene(menuScene);
            Platform.runLater(() -> stage.setFullScreen(true));
        });

        return optionsScene;
    }

    public void quit(ActionEvent e) {
        Platform.exit();
    }

    //test - push
}