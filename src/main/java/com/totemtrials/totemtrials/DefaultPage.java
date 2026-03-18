package com.totemtrials.totemtrials;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.InputStream;

public abstract class DefaultPage extends Application {

    protected Stage stage;
    protected MediaPlayer mediaPlayer;
    protected Image backgroundImage;

    protected ImageView createCroppedImageView(String imagePath, double widthRatio) {
        ImageView iv = new ImageView();
        InputStream is = getClass().getResourceAsStream(imagePath);

        if (is == null) {
            System.err.println("Image introuvable : " + imagePath);
            return iv;
        }

        Image img = new Image(is);
        PixelReader pr = img.getPixelReader();
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

    protected void loadBackground(String imagePath) {
        InputStream is = getClass().getResourceAsStream(imagePath);
        if (is == null) {
            System.err.println("Background introuvable : " + imagePath);
            return;
        }
        backgroundImage = new Image(is);
    }

    protected void loadMusic(String soundPath) {
        var res = getClass().getResource(soundPath);
        if (res != null) {
            try {
                mediaPlayer = new MediaPlayer(new Media(res.toURI().toString()));
                mediaPlayer.play();
            } catch (Exception ex) {
                System.err.println("Erreur audio : " + ex.getMessage());
            }
        } else {
            System.err.println("Fichier audio introuvable : " + soundPath);
        }
    }

    protected void applyFullScreen(String title) {
        stage.setTitle(title);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
    }
}