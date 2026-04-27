package com.totemtrials.totemtrials;

import com.totemtrials.totemtrials.controller.HomePageController;
import com.totemtrials.totemtrials.controller.SceneManager;
import com.totemtrials.totemtrials.models.Partie;
import com.totemtrials.totemtrials.view.HomePageView;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class TotemTrialsApp extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);

        Partie partie = new Partie();
        HomePageView homeView = new HomePageView(stage);
        new HomePageController(homeView, partie);

        // Stocke la scène du menu — utilisée par StopGame pour revenir
        SceneManager.setHomeScene(homeView.getScene());

        var audioRes = getClass().getResource("/sounds/Agrual.mp3");
        if (audioRes != null) {
            try {
                MediaPlayer player = new MediaPlayer(new Media(audioRes.toURI().toString()));
                player.setCycleCount(MediaPlayer.INDEFINITE);
                player.setVolume(0.25);
                player.play();
                SceneManager.setPlayer(player);
            } catch (Exception e) {
                System.err.println("[Audio] " + e.getMessage());
            }
        }

        SceneManager.show(homeView.getScene(), "Menu principal");
        stage.show();
    }
}