package com.totemtrials.totemtrials;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EndPage extends DefaultPage {

    // Chemins des jetons — à adapter selon tes ressources
    private static final String[] TOKEN_PATHS = {
            "Images/joueur/jetonElephan.png",
            "Images/joueur/jetonTigre.png",
            "Images/joueur/jetonAigle.png",
            "Images/joueur/jetonGrenouille.png" // 4ème joueur — non affiché sur podium
    };

    // Positions X/Y sur le podium issues du FXML
    private static final double[][] PODIUM_POSITIONS = {
            {481, 245},  // 1ère place
            {695, 340},  // 2ème place
            {286, 422}   // 3ème place
    };

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        loadBackground("Images/backgroundMenu.png");
        if (backgroundImage == null) return;

        loadMusic("sounds/Agrual.mp3");

        // ------------------- BACKGROUND -------------------
        ImageView imageView = new ImageView(backgroundImage);
        imageView.fitWidthProperty().bind(stage.widthProperty());
        imageView.fitHeightProperty().bind(stage.heightProperty());
        imageView.setPreserveRatio(false);

        // ------------------- TITRE -------------------
        ImageView titre = createCroppedImageView("Images/Titre-sora.png", 0.45);

        // ------------------- CLASSEMENT ALÉATOIRE -------------------
        List<Integer> ranking = buildRandomRanking(TOKEN_PATHS.length);

        // ------------------- PODIUM -------------------
        AnchorPane podium = new AnchorPane();
        podium.setPickOnBounds(false);

        for (int place = 0; place < 3; place++) {
            int playerIndex = ranking.get(place);
            ImageView token = loadTokenImageView(TOKEN_PATHS[playerIndex], 150, 150);
            if (token == null) continue;

            AnchorPane.setLeftAnchor(token, PODIUM_POSITIONS[place][0]);
            AnchorPane.setTopAnchor(token,  PODIUM_POSITIONS[place][1]);
            podium.getChildren().add(token);
        }

        // ------------------- BOUTON BACK (image) -------------------
        ImageView backButton = createCroppedImageView("Images/buttons/Exit-sora.png", 0.12);
        backButton.setCursor(Cursor.HAND);
        backButton.setOnMouseEntered(_ -> backButton.setOpacity(0.8));
        backButton.setOnMouseExited(_  -> backButton.setOpacity(1.0));
        backButton.setOnMouseClicked(_ -> {
            HomePage homePage = new HomePage();
            try {
                homePage.start(stage);
            } catch (Exception e) {
                System.err.println("Erreur retour HomePage : " + e.getMessage());
            }
        });

        AnchorPane.setRightAnchor(backButton, 30.0);
        AnchorPane.setBottomAnchor(backButton, 30.0);
        podium.getChildren().add(backButton);

        // ------------------- LAYOUT -------------------
        StackPane root = new StackPane(imageView);
        titre.setTranslateY(-280); // positionne le titre en haut

        StackPane.setAlignment(titre, Pos.CENTER);
        root.getChildren().addAll(podium, titre);

        Scene scene = new Scene(root, 1131, 742);

        var cssUrl = getClass().getResource("styles/homepage.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

        // ------------------- STAGE -------------------
        stage.setScene(scene);
        applyFullScreen("Résultats");
        stage.show();
    }

    // Génère un classement aléatoire des index joueurs
    private List<Integer> buildRandomRanking(int playerCount) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) indices.add(i);
        Collections.shuffle(indices);
        return indices;
    }

    // Charge un jeton avec taille fixe (pas de crop pixel — les jetons sont ronds, pas de bande noire)
    private ImageView loadTokenImageView(String path, double width, double height) {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.err.println("Jeton introuvable : " + path);
            return null;
        }
        ImageView iv = new ImageView(new Image(is));
        iv.setFitWidth(width);
        iv.setFitHeight(height);
        iv.setPreserveRatio(true);
        return iv;
    }
}