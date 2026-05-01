package com.totemtrials.totemtrials.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
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
    private final Scene     scene;
    private final Image     backgroundImage;

    public HomePageView(Stage stage) {

        URL classpathRoot = getClass().getClassLoader().getResource("");
        System.out.println("[HomePageView] Classpath root : " + classpathRoot);

        // ── Background ──
        // Essaie plusieurs chemins pour trouver celui qui fonctionne
        String[] candidats = {
                "/Images/backgroundMenu.png",
                "Images/backgroundMenu.png",
                "/images/backgroundMenu.png",
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
            // Dernier recours : ClassLoader sans slash
            is = getClass().getClassLoader().getResourceAsStream("Images/backgroundMenu.png");
            cheminUtilise = "ClassLoader → Images/backgroundMenu.png";
        }

        if (is == null) {
            throw new IllegalStateException(
                    "[HomePageView] backgroundMenu.png introuvable dans aucun chemin. " +
                            "Vérifie que src/main/resources est marqué 'Resources Root' dans IntelliJ."
            );
        }

        System.out.println("[HomePageView] Background chargé via : " + cheminUtilise);
        backgroundImage = new Image(is);

        ImageView bg = new ImageView(backgroundImage);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());
        bg.setPreserveRatio(false);

        // ── Boutons et titre ──
        ImageView titre = ViewUtils.createCroppedImageView(stage, "images/Titre-sora.png",          0.45);
        playButton      = ViewUtils.createCroppedImageView(stage, "images/buttons/Start-sora.png",   0.45);
        optionButton    = ViewUtils.createCroppedImageView(stage, "images/buttons/Options-sora.png", 0.38);
        quitButton      = ViewUtils.createCroppedImageView(stage, "images/buttons/Exit-sora.png",    0.28);

        VBox layout = new VBox(10, titre, playButton, optionButton, quitButton);
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(bg, layout);
        scene = new Scene(root, 600, 500);

        // ── CSS ──
        String[] cssCandidats = {
                "/styles/homepage.css",
                "/styleSheet/homepage.css",
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

    public Scene     getScene()        { return scene; }
    public Image     getBackground()   { return backgroundImage; }
    public ImageView getPlayButton()   { return playButton; }
    public ImageView getOptionButton() { return optionButton; }
    public ImageView getQuitButton()   { return quitButton; }
}