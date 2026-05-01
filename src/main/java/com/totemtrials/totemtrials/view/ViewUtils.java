package com.totemtrials.totemtrials.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.InputStream;

public class ViewUtils {

    private ViewUtils() {}

    /**
     * Charge une image depuis les ressources, rogne les marges transparentes
     * (sauf pour les GIF), et lie la largeur à un ratio de la scène.
     */
    public static ImageView createCroppedImageView(Stage stage, String imagePath, double widthRatio) {
        ImageView iv = new ImageView();
        InputStream is = ViewUtils.class.getResourceAsStream("/" + imagePath);

        if (is == null) {
            System.err.println("[ViewUtils] Image introuvable : " + imagePath);
            return iv;
        }

        Image img = new Image(is);
        iv.fitWidthProperty().bind(stage.widthProperty().multiply(widthRatio));
        iv.setPreserveRatio(true);

        // Les GIFs n'ont pas de PixelReader → pas de crop
        PixelReader pr = img.getPixelReader();
        if (imagePath.endsWith(".gif") || pr == null) {
            iv.setImage(img);
            return iv;
        }

        int w      = (int) img.getWidth();
        int h      = (int) img.getHeight();
        int top    = 0;
        int bottom = h - 1;

        outer:
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if ((pr.getArgb(x, y) >>> 24) > 10) { top = y; break outer; }
            }
        }

        outer:
        for (int y = h - 1; y >= 0; y--) {
            for (int x = 0; x < w; x++) {
                if ((pr.getArgb(x, y) >>> 24) > 10) { bottom = y; break outer; }
            }
        }

        iv.setImage(img);
        iv.setViewport(new Rectangle2D(0, top, w, bottom - top + 1));
        return iv;
    }

    public static StackPane createBackButton(Stage stage, double widthRatio) {
        ImageView img = createCroppedImageView(stage, "images/buttons/bouton-back.png", widthRatio);
        StackPane pane = new StackPane(img);
        pane.setPickOnBounds(true);
        pane.setCursor(Cursor.HAND);
        pane.setOnMouseEntered(_ -> { pane.setScaleX(1.05); pane.setScaleY(1.05); });
        pane.setOnMouseExited (_ -> { pane.setScaleX(1.0);  pane.setScaleY(1.0);  });
        return pane;
    }

}
