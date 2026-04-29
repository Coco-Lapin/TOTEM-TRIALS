package com.totemtrials.totemtrials.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.stage.Stage;

import java.io.InputStream;

public class ViewUtils {

    private ViewUtils() {}

    /**

     * Loads an image from the resources, crops the transparent margins
     * (except for GIFs), and links the width to a scene aspect ratio.

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

        // GIFs don't have a PixelReader → no cropping
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
}
