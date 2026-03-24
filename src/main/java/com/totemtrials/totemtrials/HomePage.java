package com.totemtrials.totemtrials;

import java.io.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.stage.Stage;

import javax.lang.model.type.NullType;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class HomePage extends Application {

    private Stage stage;
    private MediaPlayer mediaPlayer;
    private Image image;
    private Jeton tigre;
    private Jeton aigle;
    private Jeton serpent;
    private Jeton elephant;
    private Joueur j1;
    private Joueur j2;
    private Joueur j3;
    private Joueur j4;
    private Joueur[] mesJoueurs;
    private Jeton[] mesJetons;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        //-------- Création des jetons ---------
        tigre = new Jeton("tigre",createCroppedImageView("Images/tokkens/jetonTigre.png",0.10),createCroppedImageView("Images/tokkens/jetonTigre_anim.gif",0.10));
        aigle = new Jeton("aigle",createCroppedImageView("Images/tokkens/jetonAigle.png",0.10),createCroppedImageView("Images/tokkens/jetonAigle_anim.gif",0.10));
        serpent = new Jeton("serpent",createCroppedImageView("Images/tokkens/jetonSerpent.png",0.10),createCroppedImageView("Images/tokkens/jetonSerpent_anim.gif",0.10));
        elephant = new Jeton("elephant",createCroppedImageView("Images/tokkens/jetonElephant.png",0.10),createCroppedImageView("Images/tokkens/jetonElephant_anim.gif",0.10));
        //---------Mettre les jetons dans une liste--------
        mesJetons = new Jeton[]{tigre, aigle, serpent, elephant};

        // ------------------- BACKGROUND -------------------

        String path = "Images/backgroundMenu.png";
        InputStream is = getClass().getResourceAsStream(path);

        if (is == null) {
            System.err.println("Image background introuvable : " + path);
            try (InputStream in = getClass().getResourceAsStream("/");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String resource;
                while ((resource = reader.readLine()) != null) System.err.println(resource);
            } catch (Exception e) {
                System.err.println("Erreur chargement ressources");
            }
            return;
        }

        // ------------------- SOUND -------------------
        var res = getClass().getResource("sounds/Agrual.mp3");
        if (res != null) {
            try {
                String musicFile = res.toURI().toString();
                mediaPlayer = new MediaPlayer(new Media(musicFile));
                mediaPlayer.play();
            } catch (Exception ex) {
                System.err.println("Erreur audio : " + ex.getMessage());
            }
        } else {
            System.err.println("Fichier audio introuvable");
        }

        // ------------------- BACKGROUND IMAGE -------------------
        image = new Image(is);
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());
        imageView.setPreserveRatio(false);

        // ------------------- TITRE + BOUTONS -------------------
        ImageView titre        = createCroppedImageView("Images/Titre-sora.png",           0.45);
        ImageView playbutton   = createCroppedImageView("Images/buttons/Start-sora.png",   0.45);
        ImageView optionbutton = createCroppedImageView("Images/buttons/Options-sora.png", 0.38);
        ImageView quitbutton   = createCroppedImageView("Images/buttons/Exit-sora.png",    0.28);

        for (ImageView iv : new ImageView[]{playbutton, optionbutton, quitbutton}) {
            iv.setCursor(Cursor.HAND);
            iv.setOnMouseEntered(_ -> iv.setOpacity(0.8));
            iv.setOnMouseExited(_  -> iv.setOpacity(1.0));
        }

        // ------------------- SCENE -------------------
        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, 600, 500);

        var cssUrl = getClass().getResource("styles/homepage.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

        // ------------------- LAYOUT -------------------
        VBox fenetreBouton = new VBox(10);
        fenetreBouton.getChildren().addAll(titre, playbutton, optionbutton, quitbutton);
        fenetreBouton.setAlignment(Pos.CENTER);
        root.getChildren().add(fenetreBouton);

        // ------------------- ACTIONS -------------------
        quitbutton.setOnMouseClicked(_ -> Platform.exit());

        Scene choixJoueursScene = choixJoueurs(scene);
        playbutton.setOnMouseClicked(_->{
            stage.setScene(choixJoueursScene);
            stage.setTitle("Choix des jetons");
            stage.setFullScreenExitHint("");
            stage.setFullScreen(true);

        });

        Scene optionsScene = option(scene);
        optionbutton.setOnMouseClicked(_ -> {
            stage.setScene(optionsScene);
            stage.setTitle("OPTIONS");
            stage.setFullScreenExitHint("");
            stage.setFullScreen(true);
        });

        // ------------------- STAGE -------------------
        stage.setScene(scene);
        stage.setTitle("Menu principal");
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.show();
    }

    private ImageView createCroppedImageView(String imagePath, double widthRatio) {
        ImageView iv = new ImageView();
        InputStream is = getClass().getResourceAsStream(imagePath);

        if (is == null) {
            System.err.println("Image introuvable : " + imagePath);
            return iv;
        }

        Image img = new Image(is);

        // --- PROTECTION POUR LES GIFS ---
        // Si c'est un GIF ou si le PixelReader est indisponible, on saute le crop
        PixelReader pr = img.getPixelReader();
        if (imagePath.endsWith(".gif") || pr == null) {
            iv.setImage(img);
            // On lie quand même la largeur pour que le GIF ait la bonne taille
            iv.fitWidthProperty().bind(stage.widthProperty().multiply(widthRatio));
            iv.setPreserveRatio(true);
            return iv;
        }

        int w = (int) img.getWidth();
        int h = (int) img.getHeight();
        int top = 0;
        int bottom = h - 1;

        topLoop:
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if ((pr.getArgb(x, y) >>> 24) > 10) {
                    top = y;
                    break topLoop;
                }
            }
        }

        bottomLoop:
        for (int y = h - 1; y >= 0; y--) {
            for (int x = 0; x < w; x++) {
                if ((pr.getArgb(x, y) >>> 24) > 10) {
                    bottom = y;
                    break bottomLoop;
                }
            }
        }

        iv.setImage(img);
        iv.setViewport(new Rectangle2D(0, top, w, bottom - top + 1));
        iv.fitWidthProperty().bind(stage.widthProperty().multiply(widthRatio));
        iv.setPreserveRatio(true);
        return iv;
    }

    public Scene option(Scene menuScene) {
        ImageView imageViewOption = new ImageView(image);
        imageViewOption.fitWidthProperty().bind(stage.widthProperty());
        imageViewOption.fitHeightProperty().bind(stage.heightProperty());
        imageViewOption.setPreserveRatio(false);

        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");

        Slider volumeSlider = new Slider();
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(25);
        volumeSlider.prefWidthProperty().bind(stage.widthProperty().multiply(0.2));
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setBlockIncrement(1);

        VBox fenetreOption = new VBox(10);
        fenetreOption.getChildren().addAll(volumeSlider, backButton);
        fenetreOption.setAlignment(Pos.CENTER);
        fenetreOption.setFillWidth(false);

        StackPane rootOption = new StackPane(imageViewOption);
        rootOption.getChildren().add(fenetreOption);

        Scene optionsScene = new Scene(rootOption, 600, 500);

        var cssUrl = getClass().getResource("styles/homepage.css");
        if (cssUrl != null) optionsScene.getStylesheets().add(cssUrl.toExternalForm());

        if (mediaPlayer != null) {
            mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty().divide(100));
        }

        backButton.setOnAction(_ -> {
            stage.setScene(menuScene);
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        return optionsScene;
    }

    public Scene choixJoueurs(Scene menuScene) {

        //-----------------Creation de la scène-------------------------
        ImageView imageViewOption = new ImageView(image);
        imageViewOption.fitWidthProperty().bind(stage.widthProperty());
        imageViewOption.fitHeightProperty().bind(stage.heightProperty());
        imageViewOption.setPreserveRatio(false);
        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");
        Button bj2 = new Button("2 Joueurs");
        Button bj3 = new Button("3 Joueurs");
        Button bj4 = new Button("4 Joueurs");
        HBox boutons = new HBox(10);
        boutons.getChildren().addAll(bj2, bj3, bj4);
        boutons.setAlignment(Pos.CENTER);

        VBox fenetreOption = new VBox(10);
        fenetreOption.setAlignment(Pos.CENTER);
        fenetreOption.getChildren().addAll(backButton, boutons);

        StackPane rootChoix = new StackPane(imageViewOption);
        rootChoix.getChildren().add(fenetreOption);

        backButton.setOnAction(_ -> {
            stage.setScene(menuScene);
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        bj2.setOnAction(_ -> {
            j1 = new Joueur("Joueur 1");
            j2 = new Joueur("Joueur 2");
            mesJoueurs = new Joueur[]{j1, j2};
            stage.setScene(choixJetons(menuScene,mesJoueurs));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        bj3.setOnAction(_ -> {
            j1 = new Joueur("Joueur 1");
            j2 = new Joueur("Joueur 2");
            j3 = new Joueur("Joueur 3");
            mesJoueurs = new Joueur[]{j1, j2, j3};
            stage.setScene(choixJetons(menuScene,mesJoueurs));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        bj4.setOnAction(_ -> {
            j1 = new Joueur("Joueur 1");
            j2 = new Joueur("Joueur 2");
            j3 = new Joueur("Joueur 3");
            j4 = new Joueur("Joueur 4");
            mesJoueurs = new Joueur[]{j1, j2, j3, j4};
            stage.setScene(choixJetons(menuScene,mesJoueurs));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        return new Scene(rootChoix,600,500);
    }

    public Scene choixJetons(Scene menuScene, Joueur[] joueur){

        //-----------------Creation de la scène-------------------------
        ImageView imageViewOption = new ImageView(image);
        imageViewOption.fitWidthProperty().bind(stage.widthProperty());
        imageViewOption.fitHeightProperty().bind(stage.heightProperty());
        imageViewOption.setPreserveRatio(false);
        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");

        //----------------- Animation des jetons -----------------------
        for (Jeton j : mesJetons) {
            // On récupère l'ImageView qui contient le PNG (fixe)
            ImageView vueAffichee = j.getImageBase();

            // On stocke l'image PNG et son Viewport une fois pour toutes
            Image imageFixe = j.getImageBase().getImage();
            Rectangle2D viewportFixe = j.getImageBase().getViewport();

            // On stocke l'image GIF
            Image imageAnimée = j.getImageAnimation().getImage();

            vueAffichee.setOnMouseEntered(_ -> {
                // PASSAGE AU GIF
                vueAffichee.setImage(imageAnimée);
                vueAffichee.setViewport(null); // On enlève le crop pour voir tout le GIF
            });

            vueAffichee.setOnMouseExited(_ -> {
                // RETOUR AU PNG (L'animation s'arrête visuellement ici)
                vueAffichee.setImage(imageFixe);
                vueAffichee.setViewport(viewportFixe); // On remet le crop du PNG
            });
        }

        //----------------- Layout -------------------------------------
        HBox alignementJeton = new HBox(10);
        //--------Ajout des images dans la HBox ------------------------
        for(Jeton j : mesJetons){
            alignementJeton.getChildren().add(j.getImageBase());
        }

        alignementJeton.setAlignment(Pos.TOP_CENTER);

        VBox fenetreChoix = new VBox(10);
        fenetreChoix.getChildren().addAll(alignementJeton,backButton);
        fenetreChoix.setAlignment(Pos.CENTER);
        fenetreChoix.setFillWidth(false);

        StackPane rootChoix = new StackPane(imageViewOption);
        rootChoix.getChildren().add(fenetreChoix);

        Scene choixJetonScene = new Scene(rootChoix,600,500);

        var cssUrl = getClass().getResource("styles/homepage.css");
        if (cssUrl != null) choixJetonScene.getStylesheets().add(cssUrl.toExternalForm());

        /*for(Jeton j : mesJetons){

        }*/

        backButton.setOnAction(_ -> {
            mesJoueurs = new Joueur[]{};
            stage.setScene(choixJoueurs(menuScene));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        return choixJetonScene;
    }
}