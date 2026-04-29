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
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
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

        //-------- Token creation ---------
        tiger = new Token("tiger", "Advances 1 extra space upon winning a versus", createCroppedImageView("Images/tokkens/jetonTigre.png", 0.10), createCroppedImageView("Images/tokkens/jetonTigre_anim.gif", 0.10));
        eagle = new Token("eagle", "Advances 1 extra space for a correct answer", createCroppedImageView("Images/tokkens/jetonAigle.png", 0.10), createCroppedImageView("Images/tokkens/jetonAigle_anim.gif", 0.10));
        snake = new Token("snake", "Moves back 1 less space upon losing a versus", createCroppedImageView("Images/tokkens/jetonSerpent.png", 0.10), createCroppedImageView("Images/tokkens/jetonSerpent_anim.gif", 0.10));
        elephant = new Token("elephant", "Moves back 1 less space for a wrong answer", createCroppedImageView("Images/tokkens/jetonElephant.png", 0.10), createCroppedImageView("Images/tokkens/jetonElephant_anim.gif", 0.10));

        //--------- Put tokens in a list --------
        myTokens = new Token[]{tiger, eagle, snake, elephant};

        // ------------------- BACKGROUND -------------------

        String path = "Images/backgroundMenu.png";
        InputStream is = getClass().getResourceAsStream(path);

        if (is == null) {
            System.err.println("Background image not found: " + path);
            try (InputStream in = getClass().getResourceAsStream("/");
                 BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                String resource;
                while ((resource = reader.readLine()) != null) System.err.println(resource);
            } catch (Exception e) {
                System.err.println("Error loading resources");
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
                System.err.println("Audio error: " + ex.getMessage());
            }
        } else {
            System.err.println("Audio file not found");
        }

        // ------------------- BACKGROUND IMAGE -------------------
        image = new Image(is);
        ImageView imageView = new ImageView(image);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());
        imageView.setPreserveRatio(false);

        // ------------------- TITLE + BUTTON -------------------
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
            stage.setTitle("Token selection");
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
        stage.setTitle("Main menu");
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.show();
    }

    private ImageView createCroppedImageView(String imagePath, double widthRatio) {
        ImageView iv = new ImageView();
        InputStream is = getClass().getResourceAsStream(imagePath);

        if (is == null) {
            System.err.println("Image not found: " + imagePath);
            return iv;
        }

        Image img = new Image(is);

        // --- GIF PROTECTION ---

        // If it's a GIF or if the PixelReader is unavailable, the crop is skipped
        PixelReader pr = img.getPixelReader();
        if (imagePath.endsWith(".gif") || pr == null) {
            iv.setImage(img);
            // Still binding the width so the GIF has the correct size
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
        //---------------- Creation of button images ----------
        ImageView btn2 = createCroppedImageView("Images/buttons/2Players.png",0.2);
        ImageView btn3 = createCroppedImageView("Images/buttons/3Players.png",0.3);
        ImageView btn4 = createCroppedImageView("Images/buttons/4Players.png",0.25);

        //----------------- Scene creation -------------------------
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
            p1 = new Player("Player 1");
            p2 = new Player("Player 2");
            myPlayers = new Player[]{p1, p2};
            stage.setScene(tokenChoice(menuScene,myPlayers));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        bj3.setOnAction(_ -> {
            p1 = new Player("Player 1");
            p2 = new Player("Player 2");
            p3 = new Player("Player 3");
            myPlayers = new Player[]{p1, p2, p3};
            stage.setScene(tokenChoice(menuScene,myPlayers));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        bj4.setOnAction(_ -> {
            p1 = new Player("Player 1");
            p2 = new Player("Player 2");
            p3 = new Player("Player 3");
            p4 = new Player("Player 4");
            myPlayers = new Player[]{p1, p2, p3, p4};
            stage.setScene(tokenChoice(menuScene,myPlayers));
            Platform.runLater(() -> {
                stage.setFullScreenExitHint("");
                stage.setFullScreen(true);
            });
        });

        return new Scene(rootChoix,600,500);
    }


    // TO RECOVER AND INTEGRATE
    public Scene tokenChoice(Scene menuScene, Player[] joueur){

        //----------------- Scene creation -------------------------
        ImageView imageViewOption = new ImageView(image);
        imageViewOption.fitWidthProperty().bind(stage.widthProperty());
        imageViewOption.fitHeightProperty().bind(stage.heightProperty());
        imageViewOption.setPreserveRatio(false);
        Button backButton = new Button("BACK");
        backButton.getStyleClass().add("back-button");

        // --- TOKEN RESET ---
        for (Token j : myTokens) {
            j.getImageBase().setVisible(true);
            j.getImageBase().setDisable(false);
            j.getImageBase().setOpacity(1.0); // Just in case you touched the opacity
        }

        // Using a single-cell array to be able to modify the index within events
        int[] tourActuel = {0};

        // Label to indicate who must choose
        Label instruction = new Label("It's " + joueur[tourActuel[0]].getNom() + "'s turn");
        instruction.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");


        //----------------- Animation and token selection -----------------------
        for (Token j : myTokens) {

            // Retrieve the ImageView containing the (fixed) PNG
            ImageView vueAffichee = j.getImageBase();

            Label labelPouvoir = new Label("");
            labelPouvoir.setTextFill(Color.WHITE); // More reliable than setStyle for a test
            labelPouvoir.setStyle("-fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.5);"); // Semi-transparent background
            labelPouvoir.setMinWidth(Region.USE_PREF_SIZE);
            labelPouvoir.setMinHeight(30);

            VBox groupement = new VBox(10); // 10 pixels gap between image and text
            groupement.getChildren().addAll(vueAffichee, labelPouvoir);
            groupement.setAlignment(Pos.CENTER);

            // Storing the PNG image and its Viewport once and for all
            Image imageFixe = j.getImageBase().getImage();
            Rectangle2D viewportFixe = j.getImageBase().getViewport();
            Image imageAnimée = j.getImageAnimation().getImage();

            vueAffichee.setOnMouseEntered(_ -> {
                // SWITCHING TO GIF
                vueAffichee.setImage(imageAnimée);
                vueAffichee.setViewport(null); // Removing crop to see the whole GIF
                // displays the passive
                labelPouvoir.setText(j.getPassif());
            });

            vueAffichee.setOnMouseExited(_ -> {
                // BACK TO PNG (Animation visually stops here)
                vueAffichee.setImage(imageFixe);
                vueAffichee.setViewport(viewportFixe); // Putting back the PNG crop
                labelPouvoir.setText("");
            });

            vueAffichee.setOnMouseClicked(_ -> {
                if (tourActuel[0] < joueur.length) {
                    // 1. Assign the token to the current player
                    joueur[tourActuel[0]].setJetonJoueur(j);
                    System.out.println(joueur[tourActuel[0]].getNom() + " has chosen " + j.getNom());

                    // 2. Make the token disappear (remove it from the layout or make it invisible)
                    vueAffichee.setVisible(false);
                    vueAffichee.setDisable(true); // Prevents clicking on an invisible token

                    groupement.setVisible(false);
                    groupement.setManaged(false);

                    // 3. Move to the next player
                    tourActuel[0]++;

                    // 4. Update the text or move to the next part
                    if (tourActuel[0] < joueur.length) {
                        instruction.setText("It's " + joueur[tourActuel[0]].getNom() + "'s turn");
                    } else {
                        instruction.setText("All players have chosen!");
                        stage.setScene(menuScene);
                        stage.setFullScreen(true);
                    }
                }
            });

        }

        //----------------- Layout -------------------------------------
        HBox tokenAlignement = new HBox(10);
        //-------- Adding images to the HBox ------------------------
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