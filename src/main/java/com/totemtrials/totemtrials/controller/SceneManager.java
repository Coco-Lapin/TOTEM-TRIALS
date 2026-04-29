package com.totemtrials.totemtrials.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**

 * Single navigation point between scenes.

 * Avoids passing scene references between controllers.

 */
public class SceneManager {

    private static Stage stage;

    public static void init(Stage s) { stage = s; }

    public static void show(Scene scene, String title) {
        stage.setScene(scene);
        stage.setTitle(title);
        Platform.runLater(() -> {
            stage.setFullScreenExitHint("");
            stage.setFullScreen(true);
        });
    }

    public static Stage getStage() { return stage; }

    private static MediaPlayer player;

    public static void setPlayer(MediaPlayer p) { player = p; }
    public static MediaPlayer getPlayer()       { return player; }
}
