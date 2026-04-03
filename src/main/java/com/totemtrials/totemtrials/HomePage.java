package com.totemtrials.totemtrials;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
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
    private Token tiger;
    private Token eagle;
    private Token snake;
    private Token elephant;
    private Player p1;
    private Player p2;
    private Player p3;
    private Player p4;
    private Player[] myPlayers;
    private Token[] myTokens;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        //-------- Création des jetons ---------
        tiger = new Token("tiger",createCroppedImageView("Images/tokkens/jetonTigre.png",0.10),createCroppedImageView("Images/tokkens/jetonTigre_anim.gif",0.10));
        eagle = new Token("aigle",createCroppedImageView("Images/tokkens/jetonAigle.png",0.10),createCroppedImageView("Images/tokkens/jetonAigle_anim.gif",0.10));
        snake = new Token("serpent",createCroppedImageView("Images/tokkens/jetonSerpent.png",0.10),createCroppedImageView("Images/tokkens/jetonSerpent_anim.gif",0.10));
        elephant = new Token("elephant",createCroppedImageView("Images/tokkens/jetonElephant.png",0.10),createCroppedImageView("Images/tokkens/jetonElephant_anim.gif",0.10));
        //---------Mettre les jetons dans une liste--------
        myTokens = new Token[]{tiger, eagle, snake, elephant};

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
        VBox windowButton = new VBox(10);
        windowButton.getChildren().addAll(titre, playbutton, optionbutton, quitbutton);
        windowButton.setAlignment(Pos.CENTER);
        root.getChildren().add(windowButton);

        // ------------------- ACTIONS -------------------
        quitbutton.setOnMouseClicked(_ -> Platform.exit());

        Scene choicePlayerScene = choicePlayerScene(scene);
        playbutton.setOnMouseClicked(_->{
            stage.setScene(choicePlayerScene);
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

    public Scene choicePlayerScene(Scene menuScene) {
        //----------------Creation des images pour les boutons----------
        ImageView btn2 = createCroppedImageView("Images/buttons/2-joueurs.png",0.2);
        ImageView btn3 = createCroppedImageView("Images/buttons/3-joueurs.png",0.3);
        ImageView btn4 = createCroppedImageView("Images/buttons/4-joueurs.png",0.25);

        //-----------------Creation de la scène-------------------------
        ImageView imageViewOption = new ImageView(image);
        imageViewOption.fitWidthProperty().bind(stage.widthProperty());
        imageViewOption.fitHeightProperty().bind(stage.heightProperty());
        imageViewOption.setPreserveRatio(false);
        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");
        Button bj2 = new Button();
        bj2.setBackground(Background.EMPTY);
        bj2.setGraphic(btn2);
        bj2.setBackground(Background.EMPTY);
        Button bj3 = new Button();
        bj2.setBackground(Background.EMPTY);
        bj3.setGraphic(btn3);
        Button bj4 = new Button();
        bj4.setGraphic(btn4);
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
            p1 = new Player("Joueur 1");
            p2 = new Player("Joueur 2");
            myPlayers = new Player[]{p1, p2};
            stage.setScene(tokenChoice(menuScene,myPlayers));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        bj3.setOnAction(_ -> {
            p1 = new Player("Joueur 1");
            p2 = new Player("Joueur 2");
            p3 = new Player("Joueur 3");
            myPlayers = new Player[]{p1, p2, p3};
            stage.setScene(tokenChoice(menuScene,myPlayers));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        bj4.setOnAction(_ -> {
            p1 = new Player("Joueur 1");
            p2 = new Player("Joueur 2");
            p3 = new Player("Joueur 3");
            p4 = new Player("Joueur 4");
            myPlayers = new Player[]{p1, p2, p3, p4};
            stage.setScene(tokenChoice(menuScene,myPlayers));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        return new Scene(rootChoix,600,500);
    }

    public Scene tokenChoice(Scene menuScene, Player[] joueur){

        //-----------------Creation de la scène-------------------------
        ImageView imageViewOption = new ImageView(image);
        imageViewOption.fitWidthProperty().bind(stage.widthProperty());
        imageViewOption.fitHeightProperty().bind(stage.heightProperty());
        imageViewOption.setPreserveRatio(false);
        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");

        // --- RÉINITIALISATION DES JETONS ---
        for (Token j : myTokens) {
            j.getImageBase().setVisible(true);
            j.getImageBase().setDisable(false);
            j.getImageBase().setOpacity(1.0); // Au cas où vous auriez touché à l'opacité
        }

        // On utilise un tableau d'une case pour pouvoir modifier l'index dans les événements
        int[] tourActuel = {0};

        // Label pour indiquer qui doit choisir
        Label instruction = new Label("C'est au tour de : " + joueur[tourActuel[0]].getNom());
        instruction.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        //----------------- Animation et sélection des jetons -----------------------
        for (Token j : myTokens) {

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

            vueAffichee.setOnMouseClicked(_ -> {
                if (tourActuel[0] < joueur.length) {
                    // 1. Assigner le jeton au joueur actuel
                    joueur[tourActuel[0]].setJetonJoueur(j);
                    System.out.println(joueur[tourActuel[0]].getNom() + " a choisi " + j.getNom());

                    // 2. Faire disparaître le jeton (le retirer du layout ou le rendre invisible)
                    vueAffichee.setVisible(false);
                    vueAffichee.setDisable(true); // Empêche de cliquer sur un jeton invisible

                    // 3. Passer au joueur suivant
                    tourActuel[0]++;

                    // 4. Mettre à jour le texte ou passer à la suite
                    if (tourActuel[0] < joueur.length) {
                        instruction.setText("C'est au tour de : " + joueur[tourActuel[0]].getNom());
                    } else {
                        instruction.setText("Tous les joueurs ont choisi !");
                        stage.setScene(menuScene);
                        stage.setFullScreen(true);
                    }
                }
            });

        }

        //----------------- Layout -------------------------------------
        HBox tokenAlignement = new HBox(10);
        //--------Ajout des images dans la HBox ------------------------
        for(Token j : myTokens){
            tokenAlignement.getChildren().add(j.getImageBase());
        }

        tokenAlignement.setAlignment(Pos.TOP_CENTER);

        VBox fenetreChoix = new VBox(10);
        fenetreChoix.getChildren().addAll(instruction,tokenAlignement,backButton);
        fenetreChoix.setAlignment(Pos.CENTER);

        StackPane rootChoix = new StackPane(imageViewOption);
        rootChoix.getChildren().add(fenetreChoix);

        Scene tokenChoiceScene = new Scene(rootChoix,600,500);

        var cssUrl = getClass().getResource("styles/homepage.css");
        if (cssUrl != null) tokenChoiceScene.getStylesheets().add(cssUrl.toExternalForm());

        backButton.setOnAction(_ -> {
            myPlayers = new Player[]{};
            stage.setScene(choicePlayerScene(menuScene));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        return tokenChoiceScene;
    }
}