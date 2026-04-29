package com.totemtrials.totemtrials.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;

public class HomePageView {

    private final ImageView playButton;
    private final ImageView optionButton;
    private final ImageView quitButton;
    private final Button testFinButton;
    private final Scene scene;
    private final Image backgroundImage;

    public HomePageView(Stage stage) {

        URL classpathRoot = getClass().getClassLoader().getResource("");
        System.out.println("[HomePageView] Classpath root : " + classpathRoot);

        String[] candidats = {
                "/Images/backgroundMenu.png",
                "Images/backgroundMenu.png",
                "/com/totemtrials/totemtrials/Images/backgroundMenu.png",
        };

        InputStream is = null;
        String cheminUtilise = null;

        for (String chemin : candidats) {
            is = getClass().getResourceAsStream(chemin);
            if (is != null) {
                cheminUtilise = chemin;
                break;
            }
            System.out.println("[HomePageView] Introuvable : " + chemin);
        }

        if (is == null) {
            is = getClass().getClassLoader().getResourceAsStream("Images/backgroundMenu.png");
            cheminUtilise = "ClassLoader → Images/backgroundMenu.png";
        }

        if (is == null) {
            throw new IllegalStateException(
                    "[HomePageView] backgroundMenu.png introuvable."
            );
        }

        System.out.println("[HomePageView] Background chargé via : " + cheminUtilise);
        backgroundImage = new Image(is);

        ImageView bg = new ImageView(backgroundImage);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        ImageView titre   = ViewUtils.createCroppedImageView(stage, "com/totemtrials/totemtrials/Images/Titre-sora.png",          0.45);
        playButton        = ViewUtils.createCroppedImageView(stage, "com/totemtrials/totemtrials/Images/buttons/Start-sora.png",   0.45);
        optionButton      = ViewUtils.createCroppedImageView(stage, "com/totemtrials/totemtrials/Images/buttons/Options-sora.png", 0.38);
        quitButton        = ViewUtils.createCroppedImageView(stage, "com/totemtrials/totemtrials/Images/buttons/Exit-sora.png",    0.28);

        testFinButton = new Button("TEST END GAME");
        testFinButton.getStyleClass().add("back-button");

        VBox layout = new VBox(10, titre, playButton, optionButton, quitButton, testFinButton);
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(bg, layout);
        scene = new Scene(root, 600, 500);

        String[] cssCandidats = {
                "/styles/homepage.css",
                "/com/totemtrials/totemtrials/styles/homepage.css",
                "styles/homepage.css",
        };

        for (String cssChemin : cssCandidats) {
            URL cssUrl = getClass().getResource(cssChemin);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                System.out.println("[HomePageView] CSS chargé via : " + cssChemin);
                break;
            }
        }
    }

    public Scene     getScene()           { return scene; }
    public Image     getBackground()      { return backgroundImage; }
    public ImageView getPlayButton()      { return playButton; }
    public ImageView getOptionButton()    { return optionButton; }
    public ImageView getQuitButton()      { return quitButton; }
    public Button    getTestFinButton()   { return testFinButton; }
}