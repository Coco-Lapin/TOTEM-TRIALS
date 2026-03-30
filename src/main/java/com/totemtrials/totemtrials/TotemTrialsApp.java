package com.totemtrials.totemtrials;

import com.totemtrials.totemtrials.controller.HomePageController;
import com.totemtrials.totemtrials.controller.SceneManager;
import com.totemtrials.totemtrials.model.Partie;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class TotemTrialsApp extends Application {

    @Override
    public void start(Stage stage) {
        // 1. SceneManager connaît le stage → navigation centralisée
        SceneManager.init(stage);

        // 2. Model
        Partie partie = new Partie();

        // 3. View principale
        HomePageView homeView = new HomePageView(stage);

        // 4. Controller principal (il câble les events et crée les sous-controllers à la volée)
        new HomePageController(homeView, partie);

        // 5. Audio (optionnel — injecté séparément pour ne pas polluer View/Controller)
        var audioRes = getClass().getResource("com/totemtrials/totemtrials/resources/sounds/Agrual.mp3");
        if (audioRes != null) {
            try {
                MediaPlayer player = new MediaPlayer(new Media(audioRes.toURI().toString()));
                player.play();
                // Passer 'player' aux OptionsController si besoin de contrôle du volume
            } catch (Exception e) {
                System.err.println("[Audio] " + e.getMessage());
            }
        }

        // 6. Affichage
        SceneManager.show(homeView.getScene(), "Menu principal");
        stage.show();
    }
}
