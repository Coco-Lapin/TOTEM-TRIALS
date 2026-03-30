package com.totemtrials.totemtrials.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point unique de navigation entre les scènes.
 * Évite de passer des références de Scene entre controllers.
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
}
