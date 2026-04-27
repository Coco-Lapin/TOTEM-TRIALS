package com.totemtrials.totemtrials.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage       stage;
    private static Scene       homeScene;
    private static MediaPlayer player;

    // Volume des SFX (AudioClip) — lu par movementController à chaque son
    private static double sfxVolume = 0.8;

    public static void init(Stage s)           { stage = s; }
    public static Stage getStage()             { return stage; }

    public static void setHomeScene(Scene s)   { homeScene = s; }
    public static Scene getHomeScene()         { return homeScene; }

    public static void show(Scene scene, String title) {
        stage.setScene(scene);
        stage.setTitle(title);
        Platform.runLater(() -> {
            stage.setFullScreenExitHint("");
            stage.setFullScreen(true);
        });
    }

    public static void setPlayer(MediaPlayer p) { player = p; }
    public static MediaPlayer getPlayer()       { return player; }

    public static void   setSfxVolume(double v) { sfxVolume = v; }
    public static double getSfxVolume()         { return sfxVolume; }
}